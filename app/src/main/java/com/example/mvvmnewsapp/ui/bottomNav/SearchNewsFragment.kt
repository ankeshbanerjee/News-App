package com.example.mvvmnewsapp.ui.bottomNav

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.EditText
import android.widget.ProgressBar
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import androidx.navigation.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.mvvmnewsapp.MainActivity
import com.example.mvvmnewsapp.R
import com.example.mvvmnewsapp.adapters.NewsAdapter
import com.example.mvvmnewsapp.utils.Constants.Companion.SEARCH_NEWS_TIME_DELAY
import com.example.mvvmnewsapp.utils.Resource
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch


class SearchNewsFragment : Fragment(R.layout.fragment_search_news){
    lateinit var recyclerView: RecyclerView
    lateinit var newsAdapter: NewsAdapter
    lateinit var viewModel: NewsViewModel
    lateinit var progressBar: ProgressBar
    lateinit var editText: EditText

    private val tag: String = "SearchNewsResponse"

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = (activity as MainActivity).viewModel
        progressBar = view.findViewById(R.id.paginationProgressBar)
        recyclerView = view.findViewById(R.id.rvSearchNews)
        editText = view.findViewById(R.id.etSearch)
        setupRecyclerView()
        newsAdapter.setOnItemClickListener { article ->
            val action = SearchNewsFragmentDirections.actionSearchNewsFragmentToArticleActivity(article)
            view.findNavController().navigate(action)
        }

        var job: Job? = null
        editText.addTextChangedListener{editable ->
            job?.cancel()
            job = lifecycleScope.launch {
                delay(SEARCH_NEWS_TIME_DELAY)
                if (editable.toString().isNotEmpty()){
                    editable?.let {
                        viewModel.searchNews(it.toString())
                    }
                }
            }
        }

        viewModel.searchNewsLiveData.observe(viewLifecycleOwner, Observer { response ->
            when(response){
                is Resource.Success -> {
                    hideProgressBar()
                    response.data?.let { res ->
                        newsAdapter.differ.submitList(res.articles)
                    }
                }
                is Resource.Error -> {
                    hideProgressBar()
                    response.message?.let {
                        Log.e(tag, it)
                    }
                }
                is Resource.Loading -> {
                    showProgressBar()
                }
            }
        })
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