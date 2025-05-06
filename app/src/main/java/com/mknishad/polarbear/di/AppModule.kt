package com.mknishad.polarbear.di

import android.content.Context
import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.room.Room
import com.mknishad.polarbear.data.local.BeerDatabase
import com.mknishad.polarbear.data.local.BeerEntity
import com.mknishad.polarbear.data.remote.BeerApi
import com.mknishad.polarbear.data.remote.BeerRemoteMediator
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
  fun provideBeerDatabase(@ApplicationContext context: Context): BeerDatabase {
    return Room.databaseBuilder(
      context,
      BeerDatabase::class.java,
      "beers.db"
    ).build()
  }

  @Provides
  @Singleton
  fun provideLoggingInterceptor() = HttpLoggingInterceptor().apply {
    level = HttpLoggingInterceptor.Level.BODY
  }

  @Provides
  @Singleton
  fun provideOkHttpClient(loggingInterceptor: HttpLoggingInterceptor): OkHttpClient =
    OkHttpClient.Builder().addInterceptor(loggingInterceptor)
      .build()

  @Provides
  @Singleton
  fun provideBeerApi(okHttpClient: OkHttpClient): BeerApi {
    return Retrofit.Builder()
      .baseUrl(BeerApi.BASE_URL)
      .addConverterFactory(GsonConverterFactory.create())
      .client(okHttpClient)
      .build()
      .create(BeerApi::class.java)
  }

  @OptIn(ExperimentalPagingApi::class)
  @Provides
  @Singleton
  fun provideBeerPager(beerDb: BeerDatabase, beerApi: BeerApi): Pager<Int, BeerEntity> {
    return Pager(
      config = PagingConfig(pageSize = 20),
      remoteMediator = BeerRemoteMediator(
        beerDb = beerDb,
        beerApi = beerApi
      ),
      pagingSourceFactory = {
        beerDb.dao.pagingSource()
      }
    )
  }
}