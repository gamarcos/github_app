package br.com.gabrielmarcos.githubmvvm.plugin

import br.com.gabrielmarcos.githubmvvm.gist.GistRepository
import br.com.gabrielmarcos.githubmvvm.model.FavModel
import br.com.gabrielmarcos.githubmvvm.model.Gist
import io.reactivex.Completable
import io.reactivex.Observable
import io.reactivex.Single
import javax.inject.Inject

class GistRepositoryImpl @Inject constructor(
    private val gistService: GitService,
    private val gistDAO: GistDAO,
    private val favoritesDAO: FavoritesDAO
) : GistRepository {

    override fun getGistList(page: Int, connectionAvailability: Boolean): Single<List<Gist>> {
        return if (connectionAvailability) {
            gistService.getGists(page)
        } else
            gistDAO.getAllGists()
    }

    override fun setLocalGist(gist: List<Gist>): Completable = gistDAO.insertAll(gist)

    override fun setFavoriteGist(gist: Gist): Completable =
        favoritesDAO.insertFavGists(FavModel(0, gist.gistId))

    override fun deletFavoriteGistById(id: String): Completable = favoritesDAO.deleteFavById(id)

    override fun getGist(uuid: String, connectionAvailability: Boolean): Single<Gist> {
        return takeIf { connectionAvailability }?.run { gistService.getGist(uuid) }
            ?: gistDAO.getGistById(uuid)
    }

    override fun getSavedFavoriteGist(): Single<List<FavModel>> = favoritesDAO.getAllFavGists()
}