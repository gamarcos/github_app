package br.com.gabrielmarcos.githubmvvm.model

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import java.io.Serializable
import java.util.Date

@Entity(tableName = "gist")
data class Gist(
    @PrimaryKey
    @field:SerializedName("id") var gistId: String,
    @field:SerializedName("description") var description: String? = null,
    @field:SerializedName("created_at") var createdAt: Date? = null,
    @Expose
    @field:SerializedName("files") var files: Map<String, GistFile>? = null,
    @Embedded
    @field:SerializedName("owner") var owner: Owner = Owner(),
    @field:SerializedName("starred") var starred: Boolean = false

) : Serializable