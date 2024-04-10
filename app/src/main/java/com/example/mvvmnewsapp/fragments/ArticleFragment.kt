package com.example.mvvmnewsapp.fragments

import android.os.Bundle
import android.view.View
import android.webkit.WebView
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.navArgs
import com.example.mvvmnewsapp.MainActivity
import com.example.mvvmnewsapp.NewsViewModel
import com.example.mvvmnewsapp.R
import com.example.mvvmnewsapp.models.Article
import com.google.android.material.floatingactionbutton.FloatingActionButton
import java.io.Serializable

class ArticleFragment : Fragment(R.layout.fragment_article){

    private val args: ArticleFragmentArgs by navArgs()
    lateinit var viewModel: NewsViewModel

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = (activity as MainActivity).viewModel

        // open article in webview
        val webView: WebView = view.findViewById(R.id.webView)
        webView.loadUrl(args.article.url)

        view.findViewById<FloatingActionButton>(R.id.fab).apply {
            setOnClickListener{
                viewModel.upsert(args.article)
            }
        }
    }
}