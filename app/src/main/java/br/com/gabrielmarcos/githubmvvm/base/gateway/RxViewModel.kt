package br.com.gabrielmarcos.githubmvvm.base.gateway

import androidx.lifecycle.ViewModel
import br.com.gabrielmarcos.githubmvvm.base.rx.SchedulersFacade
import io.reactivex.Completable
import io.reactivex.Observable
import io.reactivex.Single
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import timber.log.Timber

open class RxViewModel : ViewModel() {
    private val disposables: CompositeDisposable = CompositeDisposable()

    fun addToDisposable(disposable: Disposable) {
        disposables.add(
            disposable
        )
    }

    fun disposableRxThread(
        completable: Completable
    ) {
        addToDisposable(
            completable
                .subscribeOn(SchedulersFacade.io())
                .observeOn(SchedulersFacade.ui())
                .doOnComplete { Timber.i("Do On Success: RxViewModel") }
                .doOnError { Timber.i("Do On Error: RxViewModel") }
                .subscribe(
                    { Timber.i("Subscribe On Success: RxViewModel") },
                    { Timber.i("Subscribe On Error: RxViewModel") })
        )
    }

    fun <P> disposableRxThread(
        observable: Observable<P>,
        subscribeSuccess: (result: P) -> Unit,
        subscribeError: (t: Throwable) -> Unit
    ) {
        addToDisposable(
            observable
                .subscribeOn(SchedulersFacade.io())
                .observeOn(SchedulersFacade.ui())
                .doOnError { subscribeError(it) }
                .subscribe(
                    { success -> subscribeSuccess(success) },
                    { error -> subscribeError(error) }
                )
        )
    }

    fun <P> disposableRxThread(
        single: Single<P>,
        subscribeSuccess: (result: P) -> Unit,
        subscribeError: (t: Throwable) -> Unit
    ) {
        addToDisposable(
            single
                .subscribeOn(SchedulersFacade.io())
                .observeOn(SchedulersFacade.ui())
                .doOnError { subscribeError(it) }
                .subscribe(
                    { success -> subscribeSuccess(success) },
                    { error -> subscribeError(error) }
                )
        )
    }

    override fun onCleared() {
        disposables.clear()
        super.onCleared()
    }
}