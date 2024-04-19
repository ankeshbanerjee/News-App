package com.example.mvvmnewsapp.ui.bottomNav

import android.os.Bundle
import android.view.View
import android.widget.EditText
import android.widget.ProgressBar
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.findNavController
import androidx.paging.LoadState
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.mvvmnewsapp.MainActivity
import com.example.mvvmnewsapp.R
import com.example.mvvmnewsapp.adapters.NewsAdapter
import com.example.mvvmnewsapp.databinding.FragmentSavedNewsBinding
import com.example.mvvmnewsapp.databinding.FragmentSearchNewsBinding
import com.example.mvvmnewsapp.utils.Constants.Companion.SEARCH_NEWS_TIME_DELAY
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch


class SearchNewsFragment : Fragment(R.layout.fragment_search_news){
    private lateinit var binding: FragmentSearchNewsBinding
    private lateinit var newsAdapter: NewsAdapter
    private lateinit var viewModel: NewsViewModel

    private val tag: String = "SearchNewsResponse"

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentSearchNewsBinding.bind(view)
        viewModel = (activity as MainActivity).viewModel
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
        binding.etSearch.addTextChangedListener{editable ->
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
        binding.centeredProgressBar.visibility = ProgressBar.GONE
    }

    private fun showCenteredProgressBar() {
        binding.centeredProgressBar.visibility = ProgressBar.VISIBLE
    }

    private fun showProgressBar() {
        binding.paginationProgressBar.visibility = ProgressBar.VISIBLE
    }
    private fun hideProgressBar() {
        binding.paginationProgressBar.visibility = ProgressBar.GONE
    }

    private fun setupRecyclerView (){
        newsAdapter = NewsAdapter()
        binding.rvSearchNews.apply {
            adapter = newsAdapter
            layoutManager = LinearLayoutManager(requireContext())
        }
    }
}