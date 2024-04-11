package com.example.mvvmnewsapp.ui.viewArticle

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.webkit.WebView
import android.widget.Toast
import androidx.navigation.navArgs
import com.example.mvvmnewsapp.R
import com.example.mvvmnewsapp.db.ArticleDatabase
import com.example.mvvmnewsapp.repository.NewsRepository
import com.google.android.material.floatingactionbutton.FloatingActionButton

class ArticleActivity : AppCompatActivity() {

    private val args: ArticleActivityArgs by navArgs()
    lateinit var viewModel: ArticleActivityViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_article)
        viewModel = ArticleActivityViewModel(NewsRepository(ArticleDatabase.getInstance(this)))

        // open article in webview
        val webView = findViewById<WebView>(R.id.articleWebView)
        webView.loadUrl(args.article.url)

        findViewById<FloatingActionButton>(R.id.articleFab).apply {
            setOnClickListener{
                viewModel.upsert(args.article)
                Toast.makeText(this@ArticleActivity, "Article saved", Toast.LENGTH_SHORT).show()
            }
        }
    }
}