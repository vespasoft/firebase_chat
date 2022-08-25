package com.example.firebasechat.ui.screens.main

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material3.*
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.firebasechat.model.User
import com.example.firebasechat.ui.common.composable.ToolBarComponent
import com.example.firebasechat.ui.common.ext.smallSpacer
import com.example.firebasechat.R.string as AppText

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(
    openScreen: (String) -> Unit,
    modifier: Modifier = Modifier,
    viewModel: MainViewModel = hiltViewModel()
) {
    val users = viewModel.users
    val topBarAppState = remember { TopAppBarState(0F, 0F, 0F) }
    val scrollBehavior = remember { TopAppBarDefaults.pinnedScrollBehavior(topBarAppState) }

    Surface(modifier = modifier) {
        Box(modifier = Modifier.fillMaxSize()) {
            Column(
                Modifier
                    .fillMaxSize()
                    .nestedScroll(scrollBehavior.nestedScrollConnection)
            ) {
                ToolBarComponent(
                    scrollBehavior = scrollBehavior,
                    title = {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text(
                                text = stringResource(id = AppText.chat),
                                style = MaterialTheme.typography.titleMedium
                            )
                        }
                    },
                    actions = {
                        // Info icon
                        Icon(
                            imageVector = Icons.Outlined.Settings,
                            tint = MaterialTheme.colorScheme.onSurfaceVariant,
                            modifier = Modifier
                                .clickable(onClick = {
                                    viewModel.onSettingsClick(openScreen)
                                })
                                .padding(horizontal = 12.dp, vertical = 16.dp)
                                .height(24.dp),
                            contentDescription = null
                        )
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
    }

    DisposableEffect(viewModel) {
        viewModel.addUsersListener()
        onDispose { viewModel.removeUsersListener() }
    }
}