package br.com.gabrielmarcos.githubmvvm.gist

import android.annotation.SuppressLint
import androidx.lifecycle.MutableLiveData
import br.com.gabrielmarcos.githubmvvm.base.gateway.RxViewModel
import br.com.gabrielmarcos.githubmvvm.data.Event
import br.com.gabrielmarcos.githubmvvm.model.FavModel
import br.com.gabrielmarcos.githubmvvm.model.Gist
import javax.inject.Inject

open class GistViewModel @Inject constructor(
    private val gistRepository: GistRepository
) : RxViewModel() {

    var currentPage = 0
    var connectionAvailability: Boolean = false

    private var neededUpdate: Boolean = true

    // Only For testes uuuh ugly :(
    internal var listResult: List<Gist> = emptyList()
    internal var favIdList: List<String> = emptyList()

    internal val showSnackbarMessage: MutableLiveData<Event<String>> = MutableLiveData()
    internal val gistListViewState: MutableLiveData<List<Gist>> = MutableLiveData()
    internal val gistDetailViewState: MutableLiveData<Gist> = MutableLiveData()
    internal val resultError: MutableLiveData<Event<Unit>> = MutableLiveData()
    internal val resultSuccess: MutableLiveData<Event<Unit>> = MutableLiveData()
    internal val emptyResult: MutableLiveData<Event<Unit>> = MutableLiveData()

    fun getGistList() {
        if (!neededUpdate) return
        disposableRxThread(
            gistRepository.getGistList(currentPage, connectionAvailability),
            { handleGistListResult(it) },
            { handleError(it) })
    }


    private fun handleGistListResult(gist: List<Gist>) {
        if (gist.isNotEmpty()) {
            getLocalFavoriteList()
            listResult = gist
        } else postEmptyResult()
    }

    private fun postEmptyResult() {
        emptyResult.value = Event(Unit)
    }

    fun getLocalFavoriteList() {
        disposableRxThread(
            gistRepository.getSavedFavoriteGist(),
            { getOnlyFavId(it) },
            { handleError(it) }
        )
    }

    private fun getOnlyFavId(favList: List<FavModel>) {
        val favIds = ArrayList<String>()
        favList.forEach { favIds.add(it.favId) }
        favIdList = favIds
        buildGistMapper()
    }

    private fun buildGistMapper() {
        handleListUpdate().also { handleSuccess(it) }
        resultSuccess.value = Event(Unit)
    }

    private fun handleSuccess(gists: List<Gist>) {
        gistListViewState.value = gists
        resultSuccess.value = Event(Unit)
        saveLocalResponse(gists)
    }

    private fun handleListUpdate(): List<Gist> {
        return if (gistListViewState.value.isNullOrEmpty()) {
            GistMapper.map(listResult, favIdList)
        } else {
            GistMapper.map(
                gistListViewState.value!!
                    .plus(listResult)
                    .distinct(),
                favIdList
            )
        }
    }

    fun getGist(id: String) {
        disposableRxThread(
            gistRepository.getGist(id, connectionAvailability),
            { gistSuccess(it) },
            { handleError(it) })
    }

    private fun gistSuccess(gist: Gist) {
        gistDetailViewState.value = gist
        resultSuccess.value = Event(Unit)
    }

    private fun handleError(throwable: Throwable) {
        showSnackbarMessage.value = Event(throwable.message ?: "")
        resultError.value = Event(Unit)
    }

    fun saveLocalResponse(gist: List<Gist>) {
        disposableRxThread(gistRepository.saveLocalGist(gist))
    }

    fun handleFavoriteState(gist: Gist) {
        if (gist.starred) {
            addGistFav(gist)
        } else {
            deleteGistFav(gist.gistId)
        }
    }

    private fun addGistFav(gist: Gist) = disposableRxThread(gistRepository.setFavoriteGist(gist))

    private fun deleteGistFav(gistId: String) =
        disposableRxThread(gistRepository.deletFavoriteGistById(gistId))

    fun updateGistList() {
        currentPage++
        getGistList()
    }

    fun checkOwnerFilter(ownerLogin: String?) {
        ownerLogin?.run {
            if (isNotBlank()) {
                neededUpdate = false
                filterGistByOwnerName(this)
            } else {
                postExistedGistList()
            }
        }
    }

    private fun postExistedGistList() {
        neededUpdate = true
        gistListViewState.value = listResult
    }

    @SuppressLint("DefaultLocale")
    private fun filterGistByOwnerName(ownerLogin: String) {
        val gistsFiltered: List<Gist> =
            listResult.filter {
                it.owner.login!!
                    .toUpperCase()
                    .contains(ownerLogin.toUpperCase() as CharSequence)
            }
        gistListViewState.value = gistsFiltered
    }
}
