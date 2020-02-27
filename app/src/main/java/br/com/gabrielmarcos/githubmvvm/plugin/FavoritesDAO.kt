package br.com.gabrielmarcos.githubmvvm.plugin

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import br.com.gabrielmarcos.githubmvvm.model.FavModel
import io.reactivex.Completable
import io.reactivex.Observable

@Dao
interface FavoritesDAO {
    @Query("SELECT * FROM fav")
    fun getAllFavGists(): Observable<List<FavModel>>

    @Query("DELETE FROM fav WHERE favId = :id")
    fun deleteFavById(id: String): Completable

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertFavGists(favGistId: FavModel): Completable
}