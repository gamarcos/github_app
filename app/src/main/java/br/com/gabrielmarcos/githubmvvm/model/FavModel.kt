package br.com.gabrielmarcos.githubmvvm.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

@Entity(tableName = "fav")
data class FavModel(
    @PrimaryKey(autoGenerate = true)
    val id: Int,
    @field:SerializedName("fav_name")
    val favId: String
)