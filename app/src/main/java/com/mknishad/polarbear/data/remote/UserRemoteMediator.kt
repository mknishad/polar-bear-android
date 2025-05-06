package com.mknishad.polarbear.data.remote

import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import androidx.room.withTransaction
import com.mknishad.polarbear.data.local.UserDatabase
import com.mknishad.polarbear.data.local.UserEntity
import com.mknishad.polarbear.data.mappers.toUserEntity
import retrofit2.HttpException
import java.io.IOException

@OptIn(ExperimentalPagingApi::class)
class UserRemoteMediator(
  private val userDb: UserDatabase,
  private val userApi: UserApi
) : RemoteMediator<Int, UserEntity>() {

  override suspend fun load(
    loadType: LoadType,
    state: PagingState<Int, UserEntity>
  ): MediatorResult {
    return try {
      val loadKey = when (loadType) {
        LoadType.REFRESH -> 1
        LoadType.PREPEND -> return MediatorResult.Success(
          endOfPaginationReached = true
        )

        LoadType.APPEND -> {
          val lastItem = state.lastItemOrNull()
          lastItem?.id ?: 1
        }
      }

      val users = userApi.getUsers(
        since = loadKey,
        perPage = state.config.pageSize
      )

      userDb.withTransaction {
        if (loadType == LoadType.REFRESH) {
          userDb.dao.clearAll()
        }

        val userEntities = users.map { it.toUserEntity() }
        userDb.dao.upsertAll(userEntities)
      }

      MediatorResult.Success(
        endOfPaginationReached = users.isEmpty()
      )
    } catch (e: IOException) {
      MediatorResult.Error(e)
    } catch (e: HttpException) {
      MediatorResult.Error(e)
    }
  }
}
