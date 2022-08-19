package com.example.firebasechat.ui.screens.chat

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.example.firebasechat.ui.common.ext.smallSpacer

@Composable
@ExperimentalMaterialApi
fun ChatScreen(
    openScreen: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    val username = "User name"
    Scaffold {
        Column(modifier = Modifier.fillMaxWidth().fillMaxHeight()) {

            Spacer(modifier = Modifier.smallSpacer())

            Text(text = "Hello $username!")
        }
    }
}