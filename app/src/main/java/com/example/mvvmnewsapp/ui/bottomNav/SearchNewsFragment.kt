package com.example.mvvmnewsapp.ui.bottomNav

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.Toast
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import androidx.navigation.findNavController
import androidx.paging.LoadState
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.mvvmnewsapp.MainActivity
import com.example.mvvmnewsapp.R
import com.example.mvvmnewsapp.adapters.NewsAdapter
import com.example.mvvmnewsapp.utils.Constants.Companion.QUERY_PAGE_SIZE
import com.example.mvvmnewsapp.utils.Constants.Companion.SEARCH_NEWS_TIME_DELAY
import com.example.mvvmnewsapp.utils.PaginationScrollListener
import com.example.mvvmnewsapp.utils.Resource
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch


class SearchNewsFragment : Fragment(R.layout.fragment_search_news){
    lateinit var recyclerView: RecyclerView
    lateinit var newsAdapter: NewsAdapter
    lateinit var viewModel: NewsViewModel
    lateinit var progressBar: ProgressBar
    lateinit var centeredProgressBar: ProgressBar
    lateinit var editText: EditText

    private val tag: String = "SearchNewsResponse"

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = (activity as MainActivity).viewModel
        progressBar = view.findViewById(R.id.paginationProgressBar)
        centeredProgressBar = view.findViewById(R.id.centeredProgressBar)
        recyclerView = view.findViewById(R.id.rvSearchNews)
        editText = view.findViewById(R.id.etSearch)
        setupRecyclerView()

        newsAdapter.setOnItemClickListener { article ->
            val action = SearchNewsFragmentDirections.actionSearchNewsFragmentToArticleActivity(article!!)
            view.findNavController().navigate(action)
        }

        newsAdapter.addLoadStateListener { loadState ->
            if (loadState.refresh is LoadState.Loading){
                showCenteredProgressBar()
                hideProgressBar()
            } else if (loadState.append is LoadState.Loading) {
                hideCenteredProgressBar()
                showProgressBar()
            } else if (loadState.append is LoadState.NotLoading || loadState.append is LoadState.Error || loadState.refresh is LoadState.Error || loadState.refresh is LoadState.NotLoading) {
                hideProgressBar()
                hideCenteredProgressBar()
            }
        }

        var job: Job? = null
        editText.addTextChangedListener{editable ->
            job?.cancel()
            job = lifecycleScope.launch {
                delay(SEARCH_NEWS_TIME_DELAY)
                if (editable.toString().isNotEmpty()){
                    editable?.let {
                        viewModel.searchNews(it.toString()).observe(viewLifecycleOwner) {
                            newsAdapter.submitData(viewLifecycleOwner.lifecycle, it)
                        }
                    }
                }
            }
        }
    }

    private fun hideCenteredProgressBar() {
        centeredProgressBar.visibility = ProgressBar.GONE
    }

    private fun showCenteredProgressBar() {
        centeredProgressBar.visibility = ProgressBar.VISIBLE
    }

    private fun showProgressBar() {
        progressBar.visibility = ProgressBar.VISIBLE
    }
    private fun hideProgressBar() {
        progressBar.visibility = ProgressBar.GONE
    }

    private fun setupRecyclerView (){
        newsAdapter = NewsAdapter()
        recyclerView.apply {
            adapter = newsAdapter
            layoutManager = LinearLayoutManager(requireContext())
        }
    }
}