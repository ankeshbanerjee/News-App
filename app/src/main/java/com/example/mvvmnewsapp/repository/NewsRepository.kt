package com.example.mvvmnewsapp.repository

import com.example.mvvmnewsapp.api.RetrofitInstance
import com.example.mvvmnewsapp.db.ArticleDatabase
import com.example.mvvmnewsapp.models.Article
import com.example.mvvmnewsapp.models.NewsResponse
import retrofit2.Response

class NewsRepository (val db: ArticleDatabase) {
    suspend fun getBreakingNews(countryCode: String, pageNumber: Int): Response<NewsResponse> =
        RetrofitInstance.api.getBreakingNews(countryCode, pageNumber)

    suspend fun searchNews(searchQuery: String, pageNumber: Int): Response<NewsResponse> =
        RetrofitInstance.api.searchNews(searchQuery, pageNumber)

    fun getSavedNews() = db.getArticleDao().getAllArticles()

    suspend fun upsert(article: Article) = db.getArticleDao().updateOrInsert(article)

    suspend fun delete(article: Article) = db.getArticleDao().deleteArticle(article)
}