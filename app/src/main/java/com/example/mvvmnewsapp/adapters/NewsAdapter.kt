package com.example.mvvmnewsapp.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.mvvmnewsapp.R
import com.example.mvvmnewsapp.models.Article
import org.w3c.dom.Text

class NewsAdapter: RecyclerView.Adapter<NewsAdapter.ViewHolder>() {

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view){
        val ivArticleImage: ImageView
        val tvSource: TextView
        val tvTitle: TextView
        val tvDescription: TextView
        val tvPublishedAt: TextView
        init{
            ivArticleImage = view.findViewById<ImageView>(R.id.ivArticleImage)
            tvSource = view.findViewById<TextView>(R.id.tvSource)
            tvTitle = view.findViewById<TextView>(R.id.tvTitle)
            tvDescription = view.findViewById<TextView>(R.id.tvDescription)
            tvPublishedAt = view.findViewById<TextView>(R.id.tvPublishedAt)
        }
    }

    private val diffUtil = object : DiffUtil.ItemCallback<Article>() {
        override fun areItemsTheSame(oldItem: Article, newItem: Article): Boolean = oldItem.url == newItem.url

        override fun areContentsTheSame(oldItem: Article, newItem: Article): Boolean = oldItem == newItem
    }

    val differ = AsyncListDiffer(this, diffUtil)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            LayoutInflater.from(parent.context)
            .inflate(R.layout.item_article_preview, parent, false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val article = differ.currentList[position]
        holder.apply {
            Glide.with(holder.itemView.context).load(article.urlToImage).into(ivArticleImage)
            tvSource.text = article.source.name
            tvTitle.text = article.title
            tvDescription.text = article.description
            tvPublishedAt.text = article.publishedAt
            itemView.setOnClickListener{
                onItemClickListener?.let { listener -> listener(article)}
            }
        }
    }

    private var onItemClickListener : ((Article) -> Unit)? = null

    fun setOnItemClickListener (listener: (Article) -> Unit) {
        onItemClickListener = listener
    }

    override fun getItemCount(): Int = differ.currentList.size
}