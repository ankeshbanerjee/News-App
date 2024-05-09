package com.example.mvvmnewsapp.ui.viewArticle

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.webkit.WebView
import android.widget.Toast
import androidx.activity.viewModels
import androidx.navigation.navArgs
import com.example.mvvmnewsapp.R
import com.example.mvvmnewsapp.databinding.ActivityArticleBinding
import com.example.mvvmnewsapp.db.ArticleDatabase
import com.example.mvvmnewsapp.repository.NewsRepository
import com.google.android.material.floatingactionbutton.FloatingActionButton
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ArticleActivity : AppCompatActivity() {

    private val args: ArticleActivityArgs by navArgs()
    private lateinit var binding: ActivityArticleBinding
    private val viewModel: ArticleActivityViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityArticleBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // open article in web-view
        val webView = binding.articleWebView
        if (args.article.url != null){
            webView.loadUrl(args.article.url as String)
        } else {
            Toast.makeText(this, "url doesn't exist", Toast.LENGTH_SHORT).show()
        }

        binding.articleFab.setOnClickListener{
            viewModel.upsert(args.article)
            Toast.makeText(this@ArticleActivity, "Article saved", Toast.LENGTH_SHORT).show()
        }

    }
}