package com.mknishad.polarbear.data.remote

import androidx.paging.PagingSource
import androidx.paging.PagingState
import coil3.network.HttpException
import java.io.IOException
import javax.inject.Inject

class BeerPagingSource @Inject constructor(
  val backend: BeerApi,
) : PagingSource<Int, BeerDto>() {
  override suspend fun load(
    params: LoadParams<Int>
  ): LoadResult<Int, BeerDto> {
    try {
      // Start refresh at page 1 if undefined.
      val nextPageNumber = params.key ?: 1
      val response = backend.getBeers(page = nextPageNumber, pageCount = 20)
      return LoadResult.Page(
        data = response,
        prevKey = null, // Only paging forward.
        nextKey = nextPageNumber + 1
      )
    } catch (e: IOException) {
      // IOException for network failures.
      return LoadResult.Error(e)
    } catch (e: HttpException) {
      // HttpException for any non-2xx HTTP status codes.
      return LoadResult.Error(e)
    }
  }

  override fun getRefreshKey(state: PagingState<Int, BeerDto>): Int? {
    // Try to find the page key of the closest page to anchorPosition from
    // either the prevKey or the nextKey; you need to handle nullability
    // here.
    //  * prevKey == null -> anchorPage is the first page.
    //  * nextKey == null -> anchorPage is the last page.
    //  * both prevKey and nextKey are null -> anchorPage is the
    //    initial page, so return null.
    return state.anchorPosition?.let { anchorPosition ->
      val anchorPage = state.closestPageToPosition(anchorPosition)
      anchorPage?.prevKey?.plus(1) ?: anchorPage?.nextKey?.minus(1)
    }
  }
}