package com.example.mvvmnewsapp.repository

import androidx.lifecycle.LiveData
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.liveData
import com.example.mvvmnewsapp.db.ArticleDatabase
import com.example.mvvmnewsapp.models.Article
import com.example.mvvmnewsapp.paging.NewsPagingDataSource
import com.example.mvvmnewsapp.paging.SearchNewsPagingDataSource
import com.example.mvvmnewsapp.utils.Constants.Companion.QUERY_PAGE_SIZE

class NewsRepository (val db: ArticleDatabase) {
//    suspend fun getBreakingNews(countryCode: String, pageNumber: Int): Response<NewsResponse> =
//        RetrofitInstance.api.getBreakingNews(countryCode, pageNumber)
    fun getBreakingNews() : LiveData<PagingData<Article>> = Pager(
    config = PagingConfig(pageSize = QUERY_PAGE_SIZE),
    pagingSourceFactory = { NewsPagingDataSource() }
    ).liveData

//    suspend fun searchNews(searchQuery: String, pageNumber: Int): Response<NewsResponse> =
//        RetrofitInstance.api.searchNews(searchQuery, pageNumber)

    fun searchNews(searchQuery: String): LiveData<PagingData<Article>> = Pager(
        config = PagingConfig(pageSize = QUERY_PAGE_SIZE),
        pagingSourceFactory = { SearchNewsPagingDataSource(searchQuery) }
    ).liveData

    fun getSavedNews() = db.getArticleDao().getAllArticles()

    suspend fun upsert(article: Article) = db.getArticleDao().updateOrInsert(article)

    suspend fun delete(article: Article) = db.getArticleDao().deleteArticle(article)
}