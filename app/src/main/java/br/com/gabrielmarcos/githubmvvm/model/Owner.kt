package br.com.gabrielmarcos.githubmvvm.model

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class Owner(
    @field:SerializedName("login")
    val login: String? = "",
    @field:SerializedName("avatar_url")
    val photo: String? = "",
    @field:SerializedName("url")
    val url: String? = ""
) : Serializable