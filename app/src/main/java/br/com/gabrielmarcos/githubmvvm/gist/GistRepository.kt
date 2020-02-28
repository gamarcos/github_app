package br.com.gabrielmarcos.githubmvvm.gist

import br.com.gabrielmarcos.githubmvvm.model.FavModel
import br.com.gabrielmarcos.githubmvvm.model.Gist
import io.reactivex.Completable
import io.reactivex.Observable
import io.reactivex.Single

interface GistRepository {
    fun getGistList(page: Int, connectionAvailability: Boolean): Single<List<Gist>>
    fun getGist(uuid: String, connectionAvailability: Boolean): Single<Gist>
    fun setLocalGist(gist: List<Gist>): Completable
    fun setFavoriteGist(gist: Gist): Completable
    fun deletFavoriteGistById(id: String): Completable
    fun getSavedFavoriteGist(): Single<List<FavModel>>
}