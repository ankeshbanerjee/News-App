package com.example.mvvmnewsapp.fragments

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.findNavController
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.mvvmnewsapp.MainActivity
import com.example.mvvmnewsapp.NewsViewModel
import com.example.mvvmnewsapp.R
import com.example.mvvmnewsapp.adapters.NewsAdapter
import com.google.android.material.snackbar.Snackbar

class SavedNewsFragment : Fragment(R.layout.fragment_saved_news){
    lateinit var viewModel: NewsViewModel
    lateinit var recyclerView: RecyclerView
    lateinit var newsAdapter: NewsAdapter
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = (activity as MainActivity).viewModel
        recyclerView = view.findViewById(R.id.rvSavedNews)
        setupRecyclerView()
        newsAdapter.setOnItemClickListener { article ->
            val action = SavedNewsFragmentDirections.actionSavedNewsFragmentToArticleFragment(article)
            view.findNavController().navigate(action)
        }
        viewModel.getSavedNews().observe(viewLifecycleOwner, Observer {
            newsAdapter.differ.submitList(it)
        })

        object : ItemTouchHelper.SimpleCallback(ItemTouchHelper.UP or ItemTouchHelper.DOWN,  ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT){
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean {
                return true
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val articleToDelete = newsAdapter.differ.currentList[viewHolder.adapterPosition]
                viewModel.deleteNews(articleToDelete)
                Snackbar.make(requireContext(), view, "Article Delete", Snackbar.LENGTH_LONG).apply {
                    setAction("Undo"){
                        viewModel.upsert(articleToDelete)
                    }
                    show()
                }
            }
        }.also {itemTouchHelperCallback ->
            ItemTouchHelper(itemTouchHelperCallback).attachToRecyclerView(recyclerView)
        }

    }

    private fun setupRecyclerView (){
        newsAdapter = NewsAdapter()
        recyclerView.apply {
            adapter = newsAdapter
            layoutManager = LinearLayoutManager(requireContext())
        }
    }
}