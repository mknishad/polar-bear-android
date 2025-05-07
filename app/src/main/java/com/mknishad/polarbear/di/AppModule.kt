package com.mknishad.polarbear.di

import android.content.Context
import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.room.Room
import com.mknishad.polarbear.BuildConfig
import com.mknishad.polarbear.data.local.UserDatabase
import com.mknishad.polarbear.data.local.UserEntity
import com.mknishad.polarbear.data.remote.UserApi
import com.mknishad.polarbear.data.remote.UserRemoteMediator
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class AppModule {

  @Provides
  @Singleton
  fun provideUserDatabase(@ApplicationContext context: Context): UserDatabase {
    return Room.databaseBuilder(
      context, UserDatabase::class.java, "user.db"
    ).build()
  }

  @Provides
  @Singleton
  fun provideUserApi(): UserApi {
    val authInterceptor = Interceptor { chain ->
      val req = chain.request()
      val requestHeaders =
        req.newBuilder().addHeader("Authorization", "Bearer ${BuildConfig.API_KEY}").build()
      chain.proceed(requestHeaders)
    }

    val loggingInterceptor = HttpLoggingInterceptor().apply {
      level = HttpLoggingInterceptor.Level.BODY
    }

    val httpClient =
      OkHttpClient.Builder().addInterceptor(authInterceptor).addInterceptor(loggingInterceptor)
        .build()

    return Retrofit.Builder().baseUrl(UserApi.BASE_URL)
      .addConverterFactory(GsonConverterFactory.create()).client(httpClient).build()
      .create(UserApi::class.java)
  }

  @OptIn(ExperimentalPagingApi::class)
  @Provides
  @Singleton
  fun provideUserPager(userDb: UserDatabase, userApi: UserApi): Pager<Int, UserEntity> {
    return Pager(
      config = PagingConfig(pageSize = 20),
      remoteMediator = UserRemoteMediator(userDb = userDb, userApi = userApi),
      pagingSourceFactory = {
        userDb.dao.pagingSource()
      })
  }
}