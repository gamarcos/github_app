package br.com.gabrielmarcos.githubmvvm.di.view

import br.com.gabrielmarcos.githubmvvm.gist.GistDetailFragment
import br.com.gabrielmarcos.githubmvvm.gist.GistFragment
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class FragmentModule {
    @ContributesAndroidInjector
    abstract fun contributeGistFragment(): GistFragment

    @ContributesAndroidInjector
    abstract fun contributeGistDetailFragment(): GistDetailFragment
}
