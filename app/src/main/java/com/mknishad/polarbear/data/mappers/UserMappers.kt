package com.mknishad.polarbear.data.mappers

import com.mknishad.polarbear.data.local.UserEntity
import com.mknishad.polarbear.data.remote.UserDto
import com.mknishad.polarbear.domain.User

fun UserDto.toUserEntity(): UserEntity {
  return UserEntity(
    id = id,
    name = name,
    login = login,
    avatarUrl = avatar_url
  )
}

fun UserEntity.toUser(): User {
  return User(
    id = id,
    name = name,
    login = login,
    avatarUrl = avatarUrl
  )
}
