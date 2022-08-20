package com.example.firebasechat.ui.screens.signUp

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.firebasechat.ui.common.composable.*
import com.example.firebasechat.ui.common.ext.basicButton
import com.example.firebasechat.ui.common.ext.fieldModifier
import com.example.firebasechat.ui.common.ext.smallSpacer
import com.example.firebasechat.ui.common.ext.textButton
import com.example.firebasechat.R.string as AppText

@Composable
fun SignUpScreen(
    openAndPopUp: (String, String) -> Unit,
    modifier: Modifier = Modifier,
    viewModel: SignUpViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState
    val fieldModifier = Modifier.fieldModifier()

    BasicToolbar(AppText.create_account)

    Column(
        modifier = modifier
            .fillMaxWidth()
            .fillMaxHeight()
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        BasicField(AppText.full_name_account, uiState.fullName, viewModel::onFullNameChange, fieldModifier)
        EmailField(uiState.email, viewModel::onEmailChange, fieldModifier)
        PasswordField(uiState.password, viewModel::onPasswordChange, fieldModifier)
        RepeatPasswordField(uiState.repeatPassword, viewModel::onRepeatPasswordChange, fieldModifier)

        BasicButton(AppText.create_account, Modifier.basicButton()) {
            viewModel.onSignUpClick(openAndPopUp)
        }

        Spacer(modifier = Modifier.smallSpacer())

        BasicTextButton(AppText.go_to_login, Modifier.textButton()) {
            viewModel.onLoginClick(openAndPopUp)
        }
    }
}
