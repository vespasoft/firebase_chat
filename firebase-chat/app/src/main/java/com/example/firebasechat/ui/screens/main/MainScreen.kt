package com.example.firebasechat.ui.screens.main

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.Scaffold
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.firebasechat.ui.common.composable.ActionToolbar
import com.example.firebasechat.ui.common.ext.smallSpacer
import com.example.firebasechat.ui.common.ext.toolbarActions
import com.example.firebasechat.R.drawable as AppIcon
import com.example.firebasechat.R.string as AppText

@Composable
@ExperimentalMaterialApi
fun MainScreen(
    openScreen: (String) -> Unit,
    modifier: Modifier = Modifier,
    viewModel: MainViewModel = hiltViewModel()
) {
    val users = viewModel.users

    Scaffold {
        Column(modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight()) {
            ActionToolbar(
                title = AppText.chat,
                modifier = Modifier.toolbarActions(),
                endActionIcon = AppIcon.ic_settings,
                endAction = {
                    viewModel.onSettingsClick(openScreen)
                }
            )

            Spacer(modifier = Modifier.smallSpacer())

            LazyColumn {
                items(users.values.toList(), key = { it.id }) { userItem ->
                    UserItem(
                        user = userItem,
                        onActionClick = {
                            viewModel.onUserActionClick(openScreen, userItem)
                        }
                    )
                }
            }
        }
    }

    DisposableEffect(viewModel) {
        viewModel.addUsersListener()
        onDispose { viewModel.removeUsersListener() }
    }
}