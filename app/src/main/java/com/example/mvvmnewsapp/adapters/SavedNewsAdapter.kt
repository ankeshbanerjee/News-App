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
import com.example.mvvmnewsapp.databinding.ItemArticlePreviewBinding
import com.example.mvvmnewsapp.models.Article

class SavedNewsAdapter: RecyclerView.Adapter<SavedNewsAdapter.ViewHolder>() {

    class ViewHolder(val binding: ItemArticlePreviewBinding) : RecyclerView.ViewHolder(binding.root)

    private val diffUtil = object : DiffUtil.ItemCallback<Article>() {
        override fun areItemsTheSame(oldItem: Article, newItem: Article): Boolean = oldItem.url == newItem.url

        override fun areContentsTheSame(oldItem: Article, newItem: Article): Boolean = oldItem == newItem
    }

    val differ = AsyncListDiffer(this, diffUtil)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(ItemArticlePreviewBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val article = differ.currentList[position]
        holder.apply {
            Glide.with(holder.itemView.context).load(article?.urlToImage).into(binding.ivArticleImage)
            binding.tvSource.text = article?.source?.name
            binding.tvTitle.text = article?.title
            binding.tvDescription.text = article?.description
            binding.tvPublishedAt.text = article?.publishedAt
            itemView.setOnClickListener{
                onItemClickListener?.let { listener -> listener(article)}
            }
        }
    }

    private var onItemClickListener : ((Article?) -> Unit)? = null

    fun setOnItemClickListener (listener: (Article?) -> Unit) {
        onItemClickListener = listener
    }

    override fun getItemCount(): Int = differ.currentList.size
}