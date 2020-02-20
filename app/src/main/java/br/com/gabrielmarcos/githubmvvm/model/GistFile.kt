package br.com.gabrielmarcos.githubmvvm.model

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class GistFile(
    @field:SerializedName("filename")
    var filename: String? = null,
    @field:SerializedName("type")
    var type: String? = null,
    @field:SerializedName("language")
    var language: String? = null,
    @field:SerializedName("raw_url")
    var raw: String? = null,
    @field:SerializedName("size")
    var size: Long = 0,
    @field:SerializedName("truncated")
    var truncated: Boolean? = false,
    @field:SerializedName("content")
    var content: String? = null
) : Serializable