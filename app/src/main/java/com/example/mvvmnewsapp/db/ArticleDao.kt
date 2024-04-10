package com.example.mvvmnewsapp.db

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.mvvmnewsapp.models.Article

@Dao
interface ArticleDao {
    @Query("SELECT * FROM articles_table")
    fun getAllArticles() : LiveData<List<Article>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun updateOrInsert(article: Article)

    @Delete
    suspend fun deleteArticle(article: Article)
}