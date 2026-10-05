package com.appwise.mvicleanarch.data.remote

import kotlinx.serialization.Serializable

@Serializable
data class ProductDto(
    val id: Int,
    val title: String,
    val description: String,
    val price: Double,
    val discountPercentage: Double = 0.0,
    val rating: Double = 0.0,
    val stock: Int = 0,
    val brand: String? = null,
    val category: String = "",
    val thumbnail: String = "",
    val images: List<String> = emptyList()
)

@Serializable
data class ProductResponseDto(
    val products: List<ProductDto>
)
