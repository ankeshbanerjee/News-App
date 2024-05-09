package com.example.mvvmnewsapp.di.modules

import android.content.Context
import com.example.mvvmnewsapp.db.ArticleDatabase
import com.example.mvvmnewsapp.di.HiltApplication
import com.example.mvvmnewsapp.repository.NewsRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class AppModule {

    @Provides
    @Singleton
    fun providesArticleDatabase(
        @ApplicationContext context: Context
    ) = ArticleDatabase.getInstance(context)

    @Provides
    @Singleton
    fun providesNewsRepository(db: ArticleDatabase) = NewsRepository(db)

    @Provides
    @Singleton
    fun providesHiltApplication (
        @ApplicationContext application: Context
    ) = application as HiltApplication
}