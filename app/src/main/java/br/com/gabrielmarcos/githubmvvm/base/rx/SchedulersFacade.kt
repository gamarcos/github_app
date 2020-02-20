package br.com.gabrielmarcos.githubmvvm.base.rx

import io.reactivex.Scheduler
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

object SchedulersFacade {

    fun io(): Scheduler = Schedulers.io()

    fun computation(): Scheduler = Schedulers.computation()

    fun ui(scheduler: Scheduler = AndroidSchedulers.mainThread()): Scheduler = scheduler
}