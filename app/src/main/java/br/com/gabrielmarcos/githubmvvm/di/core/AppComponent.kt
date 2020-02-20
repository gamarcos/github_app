package br.com.gabrielmarcos.githubmvvm.di.core

import android.app.Application
import br.com.gabrielmarcos.githubmvvm.App
import br.com.gabrielmarcos.githubmvvm.di.repository.RepositoryModule
import br.com.gabrielmarcos.githubmvvm.di.view.ActivityModule
import br.com.gabrielmarcos.githubmvvm.di.viewmodel.ViewModelModule
import dagger.BindsInstance
import dagger.Component
import dagger.android.AndroidInjectionModule
import javax.inject.Singleton

@Singleton
@Component(
    modules = [
        AndroidInjectionModule::class,
        AppModule::class,
        RepositoryModule::class,
        ActivityModule::class,
        ViewModelModule::class]
)
interface AppComponent {
    @Component.Builder
    interface Builder {
        @BindsInstance
        fun application(application: Application): Builder

        fun build(): AppComponent
    }

    fun inject(application: App)
}
