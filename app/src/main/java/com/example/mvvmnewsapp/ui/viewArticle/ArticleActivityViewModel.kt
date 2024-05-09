package com.example.mvvmnewsapp.ui.viewArticle

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mvvmnewsapp.models.Article
import com.example.mvvmnewsapp.repository.NewsRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ArticleActivityViewModel @Inject constructor(private val newsRepository: NewsRepository) : ViewModel() {
    fun upsert (article: Article) = viewModelScope.launch {
        newsRepository.upsert(article)
    }
}