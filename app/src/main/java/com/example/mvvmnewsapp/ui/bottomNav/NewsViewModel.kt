package com.example.mvvmnewsapp.ui.bottomNav

import android.app.Application
import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.cachedIn
import androidx.paging.liveData
import com.example.mvvmnewsapp.models.Article
import com.example.mvvmnewsapp.repository.NewsRepository
import com.example.mvvmnewsapp.paging.SearchNewsPagingDataSource
import com.example.mvvmnewsapp.utils.Constants.Companion.QUERY_PAGE_SIZE
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class NewsViewModel @Inject constructor(application: Application, private val newsRepository: NewsRepository): AndroidViewModel(application) {

    val breakingNewsLiveData: LiveData<PagingData<Article>> =
        newsRepository.getBreakingNews()
            .cachedIn(viewModelScope)
    fun searchNews(searchQuery: String): LiveData<PagingData<Article>> = Pager(
            config = PagingConfig(pageSize = QUERY_PAGE_SIZE),
            pagingSourceFactory = { SearchNewsPagingDataSource(searchQuery) }
        ).liveData.cachedIn(viewModelScope)


    // room database related functions
    fun getSavedNews() = newsRepository.getSavedNews()

    fun deleteNews(article: Article) = viewModelScope.launch {
        newsRepository.delete(article)
    }

    fun upsert (article: Article) = viewModelScope.launch {
        newsRepository.upsert(article)
    }

    // check internet connection
    fun hasInternetConnection (): Boolean{
        val context = getApplication<Application>().applicationContext
        val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            val network = connectivityManager.activeNetwork ?: return false
            val activeNetwork = connectivityManager.getNetworkCapabilities(network) ?: return false
            return when {
                activeNetwork.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> true
                activeNetwork.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> true
                else -> false
            }
        } else {
            // if the android version is below M
            @Suppress("DEPRECATION") val networkInfo =
                connectivityManager.activeNetworkInfo ?: return false
            @Suppress("DEPRECATION")
            return networkInfo.isConnected
        }
    }

}

