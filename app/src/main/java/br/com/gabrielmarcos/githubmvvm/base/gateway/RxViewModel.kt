package br.com.gabrielmarcos.githubmvvm.base.gateway

import android.annotation.SuppressLint
import androidx.lifecycle.ViewModel
import br.com.gabrielmarcos.githubmvvm.base.rx.SchedulersFacade
import io.reactivex.Completable
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
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
                .subscribeOn(SchedulersFacade.io())
                .observeOn(SchedulersFacade.ui())
                .subscribe({ Timber.i("On Success: RxViewModel") }, { subscribeError(it) })
        )
    }

    @SuppressLint("CheckResult")
    fun <P> disposableRxThread(
        single: () -> P,
        subscribe: (disposable: Disposable) -> Unit,
        subscribeSuccess: (result: P) -> Unit,
        subscribeError: (t: Throwable) -> Unit
    ) {
        Observable.fromCallable { single() }
            .subscribeOn(SchedulersFacade.io())
            .observeOn(AndroidSchedulers.mainThread())
            .doOnSubscribe { subscribe(it) }
            .doOnError { subscribeError(it) }
            .subscribe({ subscribeSuccess(it) }, { subscribeError(it) })
    }

    override fun onCleared() {
        disposables.clear()
        super.onCleared()
    }
}