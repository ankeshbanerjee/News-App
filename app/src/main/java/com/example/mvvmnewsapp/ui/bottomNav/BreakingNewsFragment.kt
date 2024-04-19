package com.example.mvvmnewsapp.ui.bottomNav

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import androidx.paging.LoadState
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.mvvmnewsapp.MainActivity
import com.example.mvvmnewsapp.R
import com.example.mvvmnewsapp.adapters.NewsAdapter
import com.example.mvvmnewsapp.databinding.FragmentBreakingNewsBinding

class BreakingNewsFragment : Fragment(R.layout.fragment_breaking_news){

    private lateinit var binding: FragmentBreakingNewsBinding
    private lateinit var viewModel: NewsViewModel
    private lateinit var newsAdapter: NewsAdapter

    private val tag = "BreakingNewsResponse"

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentBreakingNewsBinding.bind(view)
        viewModel = (activity as MainActivity).viewModel
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
        binding.paginationProgressBar.visibility = ProgressBar.VISIBLE
    }

    private fun hideProgressBar (){
        binding.paginationProgressBar.visibility = ProgressBar.GONE
    }

    private fun showCenteredProgressBar (){
        binding.centeredProgressBar.visibility = ProgressBar.VISIBLE
    }

    private fun hideCenteredProgressBar (){
        binding.centeredProgressBar.visibility = ProgressBar.GONE
    }

    private fun setupRecyclerView(){
        binding.rvBreakingNews.apply {
            newsAdapter = NewsAdapter()
            adapter = newsAdapter
            layoutManager = LinearLayoutManager(requireContext())
        }
    }
}
