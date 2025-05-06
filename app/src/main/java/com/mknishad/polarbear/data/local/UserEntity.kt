package com.mknishad.polarbear.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class UserEntity(
  @PrimaryKey
  val id: Int,
  val name: String?,
  val login: String?,
  val avatarUrl: String?,
)