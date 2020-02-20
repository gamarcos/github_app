package br.com.gabrielmarcos.githubmvvm.di.core

import br.com.gabrielmarcos.githubmvvm.App

object AppInjector {

    fun init(application: App) {
        DaggerAppComponent.builder().application(application)
            .build().inject(application)
    }
}
