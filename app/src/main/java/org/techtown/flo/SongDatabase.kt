package org.techtown.flo

import android.content.Context
import androidx.room.AutoMigration
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase

@Database(entities = [
    Song::class, User::class, Likes::class, Album::class],
    version = 2,
    exportSchema = false

)
abstract class SongDatabase: RoomDatabase() {
    abstract fun SongDao(): SongDao
    abstract fun UserDao(): UserDao
    abstract fun AlbumDao(): AlbumDao

    companion object {
        private var instance: SongDatabase? = null

        @Synchronized
        fun getInstance(context: Context): SongDatabase? {
            if (instance == null) {
                synchronized(SongDatabase::class) {
                    instance = Room.databaseBuilder(
                        context.applicationContext,
                        SongDatabase::class.java,
                        "song-database"
                    ).allowMainThreadQueries()
                        .addMigrations(MIGRATION_1_TO_2)
                        .build()
                }
            }
            return instance
        }

        private val MIGRATION_1_TO_2: Migration = object : Migration(1,2) {
            override fun migrate(database: SupportSQLiteDatabase) {
                database.run {
                    execSQL("CREATE TABLE IF NOT EXISTS AlbumTable (title TEXT, singer TEXT,coverImg INTEGER ,id INTEGER PRIMARY KEY NOT NULL)")
                    execSQL("CREATE TABLE IF NOT EXISTS LikeTable (userId INTEGER NOT NULL , albumId INTEGER NOT NULL, id INTEGER PRIMARY KEY NOT NULL)")
                }
            }
        }
    }
}