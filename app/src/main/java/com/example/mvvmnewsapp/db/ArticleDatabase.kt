package com.example.mvvmnewsapp.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.mvvmnewsapp.models.Article

@Database(entities = [Article::class], version = 1)
@TypeConverters(value = [Converters::class])
abstract class ArticleDatabase : RoomDatabase() {

    abstract fun getArticleDao(): ArticleDao

    companion object {

        private const val Database_NAME = "articles.db"

        fun getInstance(context: Context): ArticleDatabase =
            Room.databaseBuilder(
                context.applicationContext,
                ArticleDatabase::class.java,
                Database_NAME
            ).build()

    }

}