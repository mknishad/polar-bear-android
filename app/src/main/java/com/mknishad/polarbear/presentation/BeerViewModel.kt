package com.mknishad.polarbear.presentation


import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.cachedIn
import androidx.paging.map
import com.mknishad.polarbear.data.local.BeerEntity
import com.mknishad.polarbear.data.mappers.toBeer
import com.mknishad.polarbear.data.mappers.toBeerEntity
import com.mknishad.polarbear.data.remote.BeerApi
import com.mknishad.polarbear.data.remote.BeerPagingSource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.map
import javax.inject.Inject

@HiltViewModel
class BeerViewModel @Inject constructor(
  pager: Pager<Int, BeerEntity>
  //beerApi: BeerApi
) : ViewModel() {

  val beerPagingFlow = pager
    .flow
    .map { pagingData ->
      pagingData.map { it.toBeer() }
    }
    .cachedIn(viewModelScope)

  /*val flow = Pager(
    // Configure how data is loaded by passing additional properties to
    // PagingConfig, such as prefetchDistance.
    PagingConfig(pageSize = 20)
  ) {
    BeerPagingSource(beerApi)
  }.flow
    .map { it.map { it.toBeerEntity().toBeer() } }
    .cachedIn(viewModelScope)*/
}