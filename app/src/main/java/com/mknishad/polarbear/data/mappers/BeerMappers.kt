package com.mknishad.polarbear.data.mappers

import com.mknishad.polarbear.data.local.BeerEntity
import com.mknishad.polarbear.data.remote.BeerDto
import com.mknishad.polarbear.domain.Beer

fun BeerDto.toBeerEntity(): BeerEntity {
  return BeerEntity(
    id = id,
    name = name,
    tagline = tagline,
    description = description,
    firstBrewed = first_brewed,
    imageUrl = image_url
  )
}

fun BeerEntity.toBeer(): Beer {
  return Beer(
    id = id,
    name = name,
    tagline = tagline,
    description = description,
    firstBrewed = firstBrewed,
    imageUrl = imageUrl
  )
}