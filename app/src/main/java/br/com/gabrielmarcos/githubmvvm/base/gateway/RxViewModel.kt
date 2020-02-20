package br.com.gabrielmarcos.githubmvvm.base.gateway

import androidx.lifecycle.ViewModel
import io.reactivex.Completable
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import timber.log.Timber

open class RxViewModel : ViewModel() {
    private val disposables: CompositeDisposable = CompositeDisposable()

    fun addToDisposable(disposable: Disposable) {
        disposables.add(disposable)
    }

    fun disposableRxThread(
        completable: Completable,
        subscribeError: (t: Throwable) -> Unit
    ) {
        addToDisposable(
            completable
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ Timber.i("On Success: RxViewModel") }, { subscribeError(it) })
        )
    }

    fun <P> disposableRxThread(
        single: Single<P>,
        subscribe: (disposable: Disposable) -> Unit,
        subscribeSuccess: (result: P) -> Unit,
        subscribeError: (t: Throwable) -> Unit
    ) {
        addToDisposable(
            single
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe { subscribe(it) }
                .doOnSuccess { subscribeSuccess(it) }
                .doOnError { subscribeError(it) }
                .subscribe({ subscribeSuccess(it) }, { subscribeError(it) })
        )
    }

    override fun onCleared() {
        disposables.clear()
        super.onCleared()
    }
}