package com.example.firebasechat.ui.screens.login

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.firebasechat.R
import com.example.firebasechat.ui.common.composable.*
import com.example.firebasechat.ui.common.ext.*
import com.example.firebasechat.R.string as AppText

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun LoginScreen(
    openAndPopUp: (String, String) -> Unit,
    modifier: Modifier = Modifier,
    viewModel: LoginViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState

    BasicToolbar(AppText.login_details)

    Column(
        modifier = modifier
            .fillMaxWidth()
            .fillMaxHeight()
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        EmailField(uiState.email, viewModel::onEmailChange, Modifier.fieldModifier())
        PasswordField(uiState.password, viewModel::onPasswordChange, Modifier.fieldModifier())

        BasicButton(AppText.sign_in, Modifier.basicButton()) {
            viewModel.onSignInClick(openAndPopUp)
        }

        Spacer(modifier = Modifier.smallSpacer())

        LinkCardEditor(AppText.forgot_password, content = "",  Modifier.card()) {
            viewModel.onForgotPasswordClick()
        }
        LinkCardEditor(AppText.create_account_link, "", Modifier.card()) {
            viewModel.onCreateAccountClick(openAndPopUp)
        }
    }
}
