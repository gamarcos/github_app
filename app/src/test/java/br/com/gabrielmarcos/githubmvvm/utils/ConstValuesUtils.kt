package br.com.gabrielmarcos.githubmvvm.utils

import br.com.gabrielmarcos.githubmvvm.model.FavModel
import br.com.gabrielmarcos.githubmvvm.model.Gist
import br.com.gabrielmarcos.githubmvvm.model.Owner
import io.reactivex.Single
import java.util.Date

val favExpectedResponse = Single.just(
    listOf(
        FavModel(0, ""),
        FavModel(0, ""),
        FavModel(0, "")
    )
)

val gistExpectedResponse = listOf(
    Gist("12345", "", Date(), HashMap(), Owner("gabriel", "")),
    Gist("123456", "", Date(), HashMap(), Owner("alberto", "")),
    Gist("1234567", "", Date(), HashMap(), Owner("marcos", ""))
)

val starredGistExpectedValue = Gist("", "", Date(), HashMap(), Owner("", ""), starred = true)

val unstarredGistExpectedValue = Gist("", "", Date(), HashMap(), Owner("", ""), starred = false)

val gistDefault = Gist("", "", Date(), HashMap(), Owner("", ""))

val favExpectedValue = listOf(
    FavModel(0, "12345"),
    FavModel(0, "123456"),
    FavModel(0, "1234567")
)

val favIdsExpectedValue = listOf(
    "12345",
    "123456",
    "1234567"
)