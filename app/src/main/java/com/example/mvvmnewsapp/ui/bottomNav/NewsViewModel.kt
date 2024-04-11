package com.example.mvvmnewsapp.ui.bottomNav

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mvvmnewsapp.models.Article
import com.example.mvvmnewsapp.models.NewsResponse
import com.example.mvvmnewsapp.repository.NewsRepository
import com.example.mvvmnewsapp.utils.Resource
import kotlinx.coroutines.launch
import retrofit2.Response
import java.lang.Exception

class NewsViewModel(val newsRepository: NewsRepository): ViewModel() {
    val breakingNewsLiveData = MutableLiveData<Resource<NewsResponse>>()
    var breakingNewsPage = 1

    val searchNewsLiveData = MutableLiveData<Resource<NewsResponse>>()
    val searchNewsPage = 1

    init {
        getBreakingNews("us")
    }

     private fun getBreakingNews(countryCode: String) = viewModelScope.launch {
         breakingNewsLiveData.postValue(Resource.Loading())
         try {
             val response = newsRepository.getBreakingNews(countryCode, breakingNewsPage)
             breakingNewsLiveData.postValue(handleBreakingNewsResponse(response))
         } catch (e: Exception){
             breakingNewsLiveData.postValue(Resource.Error(null, e.message.toString()))
         }
     }

     fun searchNews(searchQuery: String) = viewModelScope.launch {
         searchNewsLiveData.postValue(Resource.Loading())
         try {
             val response = newsRepository.searchNews(searchQuery, searchNewsPage)
             searchNewsLiveData.postValue(handleSearchNewsResponse(response))
         } catch (e: Exception){
             searchNewsLiveData.postValue(Resource.Error(null, e.message.toString()))
         }
     }

    private fun handleBreakingNewsResponse(response: Response<NewsResponse>) : Resource<NewsResponse> {
        if (response.isSuccessful){
           response.body()?.let { res -> return Resource.Success(res) }
        }
        return Resource.Error(null, response.message())
    }

    private fun handleSearchNewsResponse(response: Response<NewsResponse>) : Resource<NewsResponse> {
        if (response.isSuccessful){
           response.body()?.let { res -> return Resource.Success(res) }
        }
        return Resource.Error(null, response.message())
    }

    // room database related functions
    fun getSavedNews() = newsRepository.getSavedNews()

    fun deleteNews(article: Article) = viewModelScope.launch {
        newsRepository.delete(article)
    }

    fun upsert (article: Article) = viewModelScope.launch {
        newsRepository.upsert(article)
    }

}