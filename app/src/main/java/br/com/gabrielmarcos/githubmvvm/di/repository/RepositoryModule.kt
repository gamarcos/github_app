package br.com.gabrielmarcos.githubmvvm.di.repository

import br.com.gabrielmarcos.githubmvvm.gist.GistRepository
import br.com.gabrielmarcos.githubmvvm.plugin.GistRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.Reusable

@Module
abstract class RepositoryModule {
    @Binds
    @Reusable
    abstract fun bindGistRepository(gistRepository: GistRepositoryImpl): GistRepository
}