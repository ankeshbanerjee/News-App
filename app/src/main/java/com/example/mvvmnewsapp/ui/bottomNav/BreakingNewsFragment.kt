package com.example.mvvmnewsapp.ui.bottomNav

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ProgressBar
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.mvvmnewsapp.MainActivity
import com.example.mvvmnewsapp.R
import com.example.mvvmnewsapp.adapters.NewsAdapter
import com.example.mvvmnewsapp.utils.PaginationScrollListener
import com.example.mvvmnewsapp.utils.Constants.Companion.COUNTRY_CODE
import com.example.mvvmnewsapp.utils.Constants.Companion.QUERY_PAGE_SIZE
import com.example.mvvmnewsapp.utils.Resource

class BreakingNewsFragment : Fragment(R.layout.fragment_breaking_news){

    lateinit var viewModel: NewsViewModel
    lateinit var progressBar: ProgressBar
    lateinit var recyclerView: RecyclerView
    lateinit var newsAdapter: NewsAdapter
    var isLoading = false
    var isLastPage = false

    private val tag = "BreakingNewsResponse"

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = (activity as MainActivity).viewModel
        progressBar = view.findViewById(R.id.paginationProgressBar)
        recyclerView = view.findViewById(R.id.rvBreakingNews)
        setupRecyclerView()
        newsAdapter.setOnItemClickListener { article ->
            val action = BreakingNewsFragmentDirections.actionBreakingNewsFragmentToArticleActivity(article)
            view.findNavController().navigate(action)
        }

        viewModel.breakingNewsLiveData.observe(viewLifecycleOwner, Observer { response ->
            when(response){
                is Resource.Success -> {
                    hideProgressBar()
                    response.data?.let{ res ->
                        newsAdapter.differ.submitList(res.articles.toList())
                        val totalPages = res.totalResults / QUERY_PAGE_SIZE + 1
                        isLastPage = viewModel.breakingNewsPage == totalPages
                        if (isLastPage){
                            recyclerView.setPadding(0, 0, 0, 0)
                        }
                    }
                }
                is Resource.Loading -> {
                    showProgressBar()
                }
                is Resource.Error -> {
                    hideProgressBar()
                    response.message?.let { msg ->
                        Log.e(tag, msg)
                        Toast.makeText(context, "Error $msg", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        })
    }

    private fun showProgressBar (){
        isLoading = true
        progressBar.visibility = ProgressBar.VISIBLE
    }

    private fun hideProgressBar (){
        isLoading = false
        progressBar.visibility = ProgressBar.GONE
    }

    private fun setupRecyclerView(){
        recyclerView.apply {
            newsAdapter = NewsAdapter()
            adapter = newsAdapter
            layoutManager = LinearLayoutManager(requireContext())
        }.also {
            it.addOnScrollListener(object : PaginationScrollListener(it.layoutManager as LinearLayoutManager){
                override fun loadMore() {
                    viewModel.getBreakingNews(COUNTRY_CODE)
                }
                override fun isLoading(): Boolean = isLoading
                override fun isLastPage(): Boolean = isLastPage
            })
        }
    }
}
