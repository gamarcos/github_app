package br.com.gabrielmarcos.githubmvvm.gist

import androidx.lifecycle.MutableLiveData
import br.com.gabrielmarcos.githubapi.data.Event
import br.com.gabrielmarcos.githubmvvm.base.gateway.RxViewModel
import br.com.gabrielmarcos.githubmvvm.model.FavModel
import br.com.gabrielmarcos.githubmvvm.model.Gist
import br.com.gabrielmarcos.githubmvvm.util.InternetUtil
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

class GistViewModel @Inject constructor(
    val gistRepository: GistRepository
) : RxViewModel() {

    var currentPage = 0

    internal val showSnackbarMessage: MutableLiveData<Event<String>> = MutableLiveData()
    internal val showLoading: MutableLiveData<Event<Unit>> = MutableLiveData()
    internal val hiddeLoading: MutableLiveData<Event<Unit>> = MutableLiveData()
    internal val gistListViewState: MutableLiveData<List<Gist>> = MutableLiveData()
    internal val gistDetailViewState: MutableLiveData<Gist> = MutableLiveData()
    internal var favoriteList: List<String> = emptyList()

    init {
        addToDisposable(
            gistRepository.getSavedFavoriteGist()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSuccess { getOnlyFavId(it) }
                .doOnError { handleFailure(it) }
                .subscribe())
    }

    private fun getOnlyFavId(favList: List<FavModel>) {
        val favIds = ArrayList<String>()
        favList.forEach { favIds.add(it.favId) }
        favoriteList = favIds
    }

    fun getGistList() {
        showLoading.value = Event(Unit)
        addToDisposable(
            gistRepository.getGistList(currentPage, InternetUtil.isInternetOn())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe { showLoading.value = Event(Unit) }
                .doOnSuccess { gistListSuccess(it) }
                .doOnError { handleFailure(it) }
                .subscribe())
    }

    fun getGist(id: String) {
        addToDisposable(
            gistRepository.getGist(id, InternetUtil.isInternetOn())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe { showLoading.value = Event(Unit) }
                .doOnSuccess { gistSuccess(it) }
                .doOnError { handleFailure(it) }
                .subscribe())
    }

    private fun gistListSuccess(gist: List<Gist>) {
        gistListViewState.value =
            takeIf { gistListViewState.value.isNullOrEmpty() }?.run {
                GistMapper.map(gist, favoriteList)
            } ?: GistMapper.map(gistListViewState.value?.run {
                plus(gist).distinct()
            } ?: gist, favoriteList)

        saveLocalResponse(gistListViewState.value?.let { it } ?: emptyList())
        hiddeLoading.value = Event(Unit)
    }

    private fun gistSuccess(gist: Gist) {
        gistDetailViewState.value = gist
        hiddeLoading.value = Event(Unit)
    }

    private fun handleFailure(throwable: Throwable) {
        showSnackbarMessage.value = Event(throwable.message ?: "")
        hiddeLoading.value = Event(Unit)
    }

    private fun saveLocalResponse(gist: List<Gist>) {
        addToDisposable(
            gistRepository.saveLocalGist(gist)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe()
        )
    }

    fun handleFavoriteState(gist: Gist) {
        takeIf { gist.starred }?.run {
            addGistFav(gist)
        } ?: deleteGistFav(gist.gistId)
    }

    private fun addGistFav(gist: Gist) {
        addToDisposable(
            gistRepository.setFavoriteGist(gist)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe()
        )
    }

    private fun deleteGistFav(gistId: String) {
        addToDisposable(
            gistRepository.deletFavoriteGistById(gistId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe()
        )
    }

    fun updateGistList() {
        currentPage++
        getGistList()
    }
}
