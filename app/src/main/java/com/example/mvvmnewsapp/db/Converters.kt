package com.example.mvvmnewsapp.db

import androidx.room.TypeConverter
import com.example.mvvmnewsapp.models.Source


class Converters {
    @TypeConverter
    fun fromSource(source: Source): String = source.name

    @TypeConverter
    fun toSource(name: String): Source = Source(name, name)
}