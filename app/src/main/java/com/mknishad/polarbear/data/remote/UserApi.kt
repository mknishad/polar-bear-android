package com.mknishad.polarbear.data.remote

import retrofit2.http.GET
import retrofit2.http.Query


interface UserApi {
  @GET("/users")
  suspend fun getUsers(@Query("since") since: Int, @Query("per_page") perPage: Int): List<UserDto>

  companion object {
    const val BASE_URL = "https://api.github.com/"
  }
}
