package br.com.gabrielmarcos.githubmvvm.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import br.com.gabrielmarcos.githubmvvm.model.FavModel
import br.com.gabrielmarcos.githubmvvm.model.Gist
import br.com.gabrielmarcos.githubmvvm.plugin.FavoritesDAO
import br.com.gabrielmarcos.githubmvvm.plugin.GistDAO

/**
 * The Room database for this app
 */
@Database(
    entities = [Gist::class, FavModel::class],
    version = 3, exportSchema = false
)
@TypeConverters(DateConverters::class, MapTypeConverter::class)
abstract class AppDatabase : RoomDatabase() {

    abstract fun repositoryDAO(): GistDAO

    abstract fun favoritesDAO(): FavoritesDAO

    companion object {

        // For Singleton instantiation
        @Volatile
        private var instance: AppDatabase? = null

        fun getInstance(context: Context): AppDatabase {
            return instance ?: synchronized(this) {
                instance ?: buildDatabase(context).also { instance = it }
            }
        }

        // Create and pre-populate the database. See this article for more details:
        // https://medium.com/google-developers/7-pro-tips-for-room-fbadea4bfbd1#4785
        private fun buildDatabase(context: Context): AppDatabase {
            return Room.databaseBuilder(context, AppDatabase::class.java, "githubgist-db")
                .build()
        }
    }
}
