package br.com.gabrielmarcos.githubmvvm

import android.app.Application
import br.com.gabrielmarcos.githubmvvm.di.core.AppInjector
import br.com.gabrielmarcos.githubmvvm.util.CrashReportingTree
import br.com.gabrielmarcos.githubmvvm.util.InternetUtil
import com.facebook.stetho.Stetho
import dagger.android.AndroidInjector
import dagger.android.DispatchingAndroidInjector
import dagger.android.HasAndroidInjector
import timber.log.Timber
import javax.inject.Inject

class App : Application(), HasAndroidInjector {

    @Inject
    internal lateinit var androidInjector: DispatchingAndroidInjector<Any>

    override fun onCreate() {
        super.onCreate()

        if (BuildConfig.DEBUG) Stetho.initializeWithDefaults(this)

        if (BuildConfig.DEBUG) Timber.plant(Timber.DebugTree())
        else Timber.plant(CrashReportingTree())

        AppInjector.init(this)
        InternetUtil.init(this)
    }

    override fun androidInjector(): AndroidInjector<Any> = androidInjector
}