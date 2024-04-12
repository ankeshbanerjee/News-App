package com.example.mvvmnewsapp.utils

import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

abstract class PaginationScrollListener (private val layoutManager: LinearLayoutManager) : RecyclerView.OnScrollListener() {
    override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
        super.onScrolled(recyclerView, dx, dy)
        val visibleItemCount = layoutManager.childCount
        val totalItemCount = layoutManager.itemCount
        val firstVisibleItemPosition = layoutManager.findFirstVisibleItemPosition()
        if (!isLoading() && !isLastPage() && firstVisibleItemPosition + visibleItemCount >= totalItemCount && firstVisibleItemPosition >= 0){
            loadMore()
        }
    }
    abstract fun loadMore()
    abstract fun isLoading(): Boolean
    abstract fun isLastPage(): Boolean
}