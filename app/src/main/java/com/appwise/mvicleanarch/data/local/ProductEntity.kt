package com.appwise.mvicleanarch.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "products")
data class ProductEntity(
    @PrimaryKey val id:Int,
    val title: String,
    val summary: String,
    val imageUrl: String
)
