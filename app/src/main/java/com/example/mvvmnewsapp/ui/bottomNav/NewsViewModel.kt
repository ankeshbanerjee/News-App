package com.example.mvvmnewsapp.ui.bottomNav

import android.app.Application
import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mvvmnewsapp.models.Article
import com.example.mvvmnewsapp.models.NewsResponse
import com.example.mvvmnewsapp.repository.NewsRepository
import com.example.mvvmnewsapp.utils.Constants
import com.example.mvvmnewsapp.utils.Constants.Companion.COUNTRY_CODE
import com.example.mvvmnewsapp.utils.Resource
import kotlinx.coroutines.launch
import retrofit2.Response
import java.lang.Exception

class NewsViewModel(application: Application , val newsRepository: NewsRepository): AndroidViewModel(application) {
    val breakingNewsLiveData = MutableLiveData<Resource<NewsResponse>>()
    var breakingNewsPage = 1
    var breakingNewsResponse : NewsResponse? = null

    val searchNewsLiveData = MutableLiveData<Resource<NewsResponse>>()
    var searchNewsPage = 1
    var searchNewsResponse : NewsResponse? = null

    init {
        getBreakingNews(COUNTRY_CODE)
    }
    fun getBreakingNews(countryCode: String) = viewModelScope.launch {
        breakingNewsLiveData.postValue(Resource.Loading())
        if (hasInternetConnection()){
             try {
                 val response = newsRepository.getBreakingNews(countryCode, breakingNewsPage)
                 breakingNewsLiveData.postValue(handleBreakingNewsResponse(response))
             } catch (e: Exception){
                 breakingNewsLiveData.postValue(Resource.Error(null, e.message.toString()))
             }
         }else {
             breakingNewsLiveData.postValue(Resource.Error(null, "No internet connection"))
         }
     }

     fun searchNews(searchQuery: String) = viewModelScope.launch {
         if (hasInternetConnection()){
             searchNewsLiveData.postValue(Resource.Loading())
             try {
                 val response = newsRepository.searchNews(searchQuery, searchNewsPage)
                 searchNewsLiveData.postValue(handleSearchNewsResponse(response))
             } catch (e: Exception){
                 searchNewsLiveData.postValue(Resource.Error(null, e.message.toString()))
             }
         } else{
             searchNewsLiveData.postValue(Resource.Error(null, "No internet connection"))
         }
     }

    private fun handleBreakingNewsResponse(response: Response<NewsResponse>) : Resource<NewsResponse> {
        if (response.isSuccessful){
            breakingNewsPage++
            response.body()?.let { res ->
                if (breakingNewsResponse == null){
                    breakingNewsResponse = res
                } else {
                    breakingNewsResponse?.articles?.addAll(res.articles)
                }
                return Resource.Success(breakingNewsResponse ?: res)
            }
        }
        return Resource.Error(null, response.message())
    }

    private fun handleSearchNewsResponse(response: Response<NewsResponse>) : Resource<NewsResponse> {
        if (response.isSuccessful){
            searchNewsPage++
           response.body()?.let { res ->
               if (searchNewsResponse == null){
                   searchNewsResponse = res
               } else {
                   searchNewsResponse?.articles?.addAll(res.articles)
               }
               return Resource.Success(searchNewsResponse ?: res)
           }
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

    // check internet connection
    private fun hasInternetConnection (): Boolean{
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