package com.appwise.mvicleanarch.data.mapper

import com.appwise.mvicleanarch.data.local.ProductEntity
import com.appwise.mvicleanarch.data.remote.ProductDto
import com.appwise.mvicleanarch.domain.model.Product

fun ProductDto.toEntity() = ProductEntity(
    id = id, title = title, summary = summary, imageUrl = imageUrl

)

fun ProductEntity.toDomain() = Product(
    id = id, title = title, summary = summary, imageUrl = imageUrl
)