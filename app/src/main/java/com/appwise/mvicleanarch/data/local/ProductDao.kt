package com.appwise.mvicleanarch.data.local

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Upsert
import kotlinx.coroutines.flow.Flow

@Dao
abstract class ProductDao {
    @Query("SELECT * FROM products ORDER BY id")
    abstract fun observeAll(): Flow<List<ProductEntity>>

    @Upsert
    abstract suspend fun upsertAll(products: List<ProductEntity>)

    @Query("DELETE FROM products")
    abstract suspend fun clearAll()

    @Transaction
    open suspend fun replaceAll(products: List<ProductEntity>) {
        clearAll()
        upsertAll(products)
    }
}