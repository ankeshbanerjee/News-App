package com.example.mvvmnewsapp.paging

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.example.mvvmnewsapp.api.RetrofitInstance
import com.example.mvvmnewsapp.models.Article
import com.example.mvvmnewsapp.utils.Constants.Companion.COUNTRY_CODE
import retrofit2.HttpException
import java.io.IOException

class NewsPagingDataSource (): PagingSource<Int, Article>() {

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Article> {
        val position = params.key ?: 1
        return try {
            val response = RetrofitInstance.api.getBreakingNews(COUNTRY_CODE, position)
               val articles = response.body()?.articles
               LoadResult.Page(
                   data = articles?.toList() ?: listOf(),
                   prevKey = if (position == 1) null else position - 1,
                   nextKey = if (articles?.isEmpty() == true) null else position + 1
               )
        }catch (exception: IOException) {
              return LoadResult.Error(exception)
          } catch (exception: HttpException) {
              return LoadResult.Error(exception)
          }
    }

    override fun getRefreshKey(state: PagingState<Int, Article>): Int? {
        return state.anchorPosition?.let {
            state.closestPageToPosition(it)?.prevKey?.plus(1)
                ?: state.closestPageToPosition(it)?.nextKey?.minus(1)
        }
    }
}