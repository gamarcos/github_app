package br.com.gabrielmarcos.githubmvvm.plugin

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import br.com.gabrielmarcos.githubmvvm.model.Gist
import io.reactivex.Completable
import io.reactivex.Observable
import io.reactivex.Single

@Dao
interface GistDAO {
    @Query("SELECT * FROM gist")
    fun getAllGists(): Single<List<Gist>>

    @Query("SELECT * FROM gist WHERE gistId = :id")
    fun getGistById(id: String): Single<Gist>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(gists: List<Gist>): Completable

    @Query("UPDATE gist SET starred=:isStarred WHERE gistId = :id")
    fun updateGist(isStarred: Boolean, id: String): Completable

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(gist: Gist)
}