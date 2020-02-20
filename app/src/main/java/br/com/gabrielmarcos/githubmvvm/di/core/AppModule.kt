package br.com.gabrielmarcos.githubmvvm.di.core

import android.app.Application
import br.com.gabrielmarcos.githubmvvm.di.qualifiers.CoroutineScropeIO
import br.com.gabrielmarcos.githubmvvm.di.qualifiers.GitAPI
import br.com.gabrielmarcos.githubmvvm.BuildConfig
import br.com.gabrielmarcos.githubmvvm.db.AppDatabase
import br.com.gabrielmarcos.githubmvvm.di.viewmodel.ViewModelModule
import br.com.gabrielmarcos.githubmvvm.plugin.FavoritesDAO
import br.com.gabrielmarcos.githubmvvm.plugin.GistDAO
import br.com.gabrielmarcos.githubmvvm.plugin.GistRepositoryImpl
import br.com.gabrielmarcos.githubmvvm.plugin.GitService
import dagger.Module
import dagger.Provides
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module(includes = [ViewModelModule::class, CoreDataModule::class])
class AppModule {

    @Singleton
    @Provides
    fun provideGitApiService(
        @GitAPI okhttpClient: OkHttpClient,
        converterFactory: GsonConverterFactory
    ) = provideService(okhttpClient, converterFactory, GitService::class.java)

    @Singleton
    @Provides
    fun provideGistRepositoryImpl(gitService: GitService,
        gistDAO: GistDAO, favoritesDAO: FavoritesDAO) = GistRepositoryImpl(gitService, gistDAO, favoritesDAO)

    @GitAPI
    @Provides
    fun providePrivateOkHttpClient(
        upstreamClient: OkHttpClient
    ): OkHttpClient {
        return upstreamClient.newBuilder().build()
    }

    @Singleton
    @Provides
    fun provideDb(app: Application) = AppDatabase.getInstance(app)

    @Singleton
    @Provides
    fun provideGistDAO(db: AppDatabase) = db.repositoryDAO()

    @Singleton
    @Provides
    fun provideFavoriteDAO(db: AppDatabase) = db.favoritesDAO()

    @CoroutineScropeIO
    @Provides
    fun provideCoroutineScopeIO() = CoroutineScope(Dispatchers.IO)

    private fun createRetrofit(
        okhttpClient: OkHttpClient,
        converterFactory: GsonConverterFactory
    ): Retrofit {
        return Retrofit.Builder()
            .baseUrl(BuildConfig.GIT_API)
            .client(okhttpClient)
            .addConverterFactory(converterFactory)
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .build()
    }

    private fun <T> provideService(
        okhttpClient: OkHttpClient,
        converterFactory: GsonConverterFactory,
        clazz: Class<T>
    ): T {
        return createRetrofit(okhttpClient, converterFactory).create(clazz)
    }
}
