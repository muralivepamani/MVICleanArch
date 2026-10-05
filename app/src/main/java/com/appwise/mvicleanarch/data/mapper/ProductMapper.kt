package com.appwise.mvicleanarch.data.mapper

import com.appwise.mvicleanarch.data.remote.ProductDto
import com.appwise.mvicleanarch.domain.model.Product


fun ProductDto.toDomain(): Product =
    Product(
        id = id,
        title = title,
        description = description,
        price = price,
        rating = rating,
        thumbnail = thumbnail,
        images = images
    )