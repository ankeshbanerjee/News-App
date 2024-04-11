package com.example.mvvmnewsapp.ui.viewArticle

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.mvvmnewsapp.repository.NewsRepository

class ArticleActivityViewModelFactory(val newsRepository: NewsRepository): ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return ArticleActivityViewModel(newsRepository) as T
    }
}