package br.com.gabrielmarcos.githubmvvm.gist

import android.hardware.camera2.CameraManager
import androidx.lifecycle.MutableLiveData
import br.com.gabrielmarcos.githubmvvm.base.gateway.RxViewModel
import br.com.gabrielmarcos.githubmvvm.data.Event
import br.com.gabrielmarcos.githubmvvm.model.FavModel
import br.com.gabrielmarcos.githubmvvm.model.Gist
import br.com.gabrielmarcos.githubmvvm.util.InternetUtil
import javax.inject.Inject

class GistViewModel @Inject constructor(
    private val gistRepository: GistRepository
) : RxViewModel() {

    var currentPage = 0
    var connectionAvailability: Boolean = true
    internal val showSnackbarMessage: MutableLiveData<Event<String>> = MutableLiveData()
    internal val showLoading: MutableLiveData<Event<Unit>> = MutableLiveData()
    internal val gistListViewState: MutableLiveData<List<Gist>> = MutableLiveData()
    internal val gistDetailViewState: MutableLiveData<Gist> = MutableLiveData()
    internal val resultError: MutableLiveData<Event<Unit>> = MutableLiveData()
    internal val resultSuccess: MutableLiveData<Event<Unit>> = MutableLiveData()
    internal var favoriteList: List<String> = emptyList()

    fun getLocalFavoriteList() {
        disposableRxThread(
            gistRepository.getSavedFavoriteGist(),
            { showLoading.value = Event(Unit) },
            { getOnlyFavId(it) },
            { handleError(it) })
        getGistList()
    }

    private fun getOnlyFavId(favList: List<FavModel>) {
        val favIds = ArrayList<String>()
        favList.forEach { favIds.add(it.favId) }
        favoriteList = favIds
    }

    fun getGistList() {
        disposableRxThread(
            gistRepository.getGistList(currentPage, connectionAvailability),
            { showLoading.value = Event(Unit) },
            { gistListSuccess(it) },
            { handleError(it) })
    }

    fun getGist(id: String) {
        disposableRxThread(
            gistRepository.getGist(id, InternetUtil.isInternetOn()), { },
            { gistSuccess(it) },
            { handleError(it) })
    }

    private fun gistListSuccess(gist: List<Gist>) {
        gistListViewState.value = handleListUpdate(gist)
        saveLocalResponse(gistListViewState.value?.let { it } ?: emptyList())
        resultSuccess.value = Event(Unit)
    }

    private fun handleListUpdate(gist: List<Gist>): List<Gist> {
        return takeIf { gistListViewState.value.isNullOrEmpty() }?.run {
            GistMapper.map(gist, favoriteList)
        } ?: GistMapper.map(gistListViewState.value?.run {
            plus(gist).distinct()
        } ?: gist, favoriteList)
    }

    private fun gistSuccess(gist: Gist) {
        gistDetailViewState.value = gist
        resultSuccess.value = Event(Unit)
    }

    private fun handleError(throwable: Throwable) {
        showSnackbarMessage.value = Event(throwable.message ?: "")
        resultError.value = Event(Unit)
    }

    private fun saveLocalResponse(gist: List<Gist>) =
        disposableRxThread(gistRepository.saveLocalGist(gist), { handleError(it) })

    fun handleFavoriteState(gist: Gist) {
        takeIf { gist.starred }?.run {
            addGistFav(gist)
        } ?: deleteGistFav(gist.gistId)
    }

    private fun addGistFav(gist: Gist) =
        disposableRxThread(gistRepository.setFavoriteGist(gist), { handleError(it) })

    private fun deleteGistFav(gistId: String) =
        disposableRxThread(gistRepository.deletFavoriteGistById(gistId), { handleError(it) })

    fun updateGistList() {
        currentPage++
        getGistList()
    }

    fun filterGistOwner(ownerLogin: String?) {
        //TODO Searchview needed share viewmodel between activity and fragment
    }
}
