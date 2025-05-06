package com.mknishad.polarbear.presentation

import android.widget.Toast
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.paging.LoadState
import androidx.paging.compose.LazyPagingItems
import com.mknishad.polarbear.R
import com.mknishad.polarbear.domain.User


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UserScreen(
  users: LazyPagingItems<User>
) {
  val context = LocalContext.current
  LaunchedEffect(key1 = users.loadState) {
    if (users.loadState.refresh is LoadState.Error) {
      Toast.makeText(
        context,
        "Error: " + (users.loadState.refresh as LoadState.Error).error.message,
        Toast.LENGTH_LONG
      ).show()
    }
  }

  Scaffold(
    topBar = {
      TopAppBar(title = { Text(text = stringResource(R.string.app_name)) })
    }, modifier = Modifier.fillMaxSize()
  ) { paddingValues ->
    Box(
      modifier = Modifier
        .fillMaxSize()
        .padding(paddingValues), contentAlignment = Alignment.Center
    ) {
      Column {
        LazyColumn(
          modifier = Modifier.fillMaxSize(), horizontalAlignment = Alignment.CenterHorizontally
        ) {
          items(users.itemCount) { index ->
            val user = users[index]
            if (user != null) {
              UserItem(
                user = user,
                modifier = Modifier
                  .fillMaxWidth()
                  .padding(horizontal = 16.dp, vertical = 8.dp)
              )
            }
          }

          item {
            if (users.loadState.append is LoadState.Loading) {
              Box(modifier = Modifier.padding(16.dp)) {
                CircularProgressIndicator()
              }
            }
          }
        }
      }

      if (users.loadState.refresh is LoadState.Loading) {
        CircularProgressIndicator(modifier = Modifier.padding(16.dp))
      }
    }
  }
}