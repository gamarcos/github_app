package br.com.gabrielmarcos.githubmvvm.di.view

import br.com.gabrielmarcos.githubmvvm.base.view.MainActivity
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class ActivityModule {
    @ContributesAndroidInjector(modules = [FragmentModule::class])
    abstract fun contributeMainActivity(): MainActivity
}
