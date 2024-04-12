package com.example.mvvmnewsapp.ui.viewArticle

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mvvmnewsapp.models.Article
import com.example.mvvmnewsapp.repository.NewsRepository
import kotlinx.coroutines.launch

class ArticleActivityViewModel(val newsRepository: NewsRepository) : ViewModel() {
    fun upsert (article: Article) = viewModelScope.launch {
        newsRepository.upsert(article)
    }
}