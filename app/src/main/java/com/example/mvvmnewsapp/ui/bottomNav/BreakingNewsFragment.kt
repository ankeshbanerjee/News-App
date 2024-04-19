package com.example.mvvmnewsapp.ui.bottomNav

import android.os.Bundle
import android.view.View
import android.widget.ProgressBar
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import androidx.paging.LoadState
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.mvvmnewsapp.MainActivity
import com.example.mvvmnewsapp.R
import com.example.mvvmnewsapp.adapters.NewsAdapter

class BreakingNewsFragment : Fragment(R.layout.fragment_breaking_news){

    lateinit var viewModel: NewsViewModel
    lateinit var progressBar: ProgressBar
    lateinit var centeredProgressBar: ProgressBar
    lateinit var recyclerView: RecyclerView
    lateinit var newsAdapter: NewsAdapter

    private val tag = "BreakingNewsResponse"

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = (activity as MainActivity).viewModel
        progressBar = view.findViewById(R.id.paginationProgressBar)
        centeredProgressBar = view.findViewById(R.id.centeredProgressBar)
        recyclerView = view.findViewById(R.id.rvBreakingNews)
        setupRecyclerView()
        newsAdapter.setOnItemClickListener { article ->
            val action = BreakingNewsFragmentDirections.actionBreakingNewsFragmentToArticleActivity(article!!)
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

        viewModel.breakingNewsLiveData.observe(viewLifecycleOwner) {
            newsAdapter.submitData(viewLifecycleOwner.lifecycle, it)
        }
    }

    private fun showProgressBar (){
        progressBar.visibility = ProgressBar.VISIBLE
    }

    private fun hideProgressBar (){
        progressBar.visibility = ProgressBar.GONE
    }

    private fun showCenteredProgressBar (){
        centeredProgressBar.visibility = ProgressBar.VISIBLE
    }

    private fun hideCenteredProgressBar (){
        centeredProgressBar.visibility = ProgressBar.GONE
    }

    private fun setupRecyclerView(){
        recyclerView.apply {
            newsAdapter = NewsAdapter()
            adapter = newsAdapter
            layoutManager = LinearLayoutManager(requireContext())
        }
    }
}
