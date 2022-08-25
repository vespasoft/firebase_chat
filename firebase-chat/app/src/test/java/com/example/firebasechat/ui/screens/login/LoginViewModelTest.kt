package com.example.firebasechat.ui.screens.login

import com.example.firebasechat.CoroutinesTestRules
import com.example.firebasechat.LOGIN_SCREEN
import com.example.firebasechat.MAIN_SCREEN
import com.example.firebasechat.SIGN_UP_SCREEN
import com.example.firebasechat.model.repository.AccountRepository
import com.example.firebasechat.model.repository.LogRepository
import com.example.firebasechat.ui.common.ext.isValidEmail
import io.mockk.*
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class LoginViewModelTest {
    @ExperimentalCoroutinesApi
    @get:Rule
    var coroutinesTestRule = CoroutinesTestRules()

    private val accountRepository: AccountRepository = mockk(relaxed = true)
    private val logRepository: LogRepository = mockk(relaxed = true)

    private lateinit var loginViewModel : LoginViewModel

    @Before
    fun setUp() {
        MockKAnnotations.init(this)
        mockkStatic("com.example.firebasechat.ui.common.ext.StringExtKt")

        every { any<String>().isValidEmail() } returns true

        loginViewModel = LoginViewModel(accountRepository, logRepository)
    }

    @Before
    fun after() {
        clearAllMocks()
    }

    @Test
    fun `when all parameters are correct  lambda should be equals to expected`() =
        coroutinesTestRule.dispatcher.run {
            val originExpected = MAIN_SCREEN
            val destinyExpected = LOGIN_SCREEN
            var originResult = ""
            var destinyResult = ""

            coEvery { accountRepository.authenticate(any(), any(), any()) } answers {
                thirdArg<(Throwable?) -> Unit>().invoke(null)
            }

            loginViewModel.onEmailChange("vespasoft@gmail.com")
            loginViewModel.onPasswordChange("123456")

            loginViewModel.onSignInClick { origin, destiny ->
                originResult = origin
                destinyResult = destiny
            }

            assert(originResult == originExpected)
            assert(destinyResult == destinyExpected)
    }

    @Test
    fun `when email is invalid  should be equals to expected`() =
        coroutinesTestRule.dispatcher.run {
            val expected = ""
            var result = ""

            loginViewModel.onEmailChange("invalidemail")

            loginViewModel.onSignInClick { param1, param2 ->
                result = param1
            }

            assert(result == expected)
    }

    @Test
    fun `when password is invalid  should be equals to expected`() =
        coroutinesTestRule.dispatcher.run {
            val expected = ""
            var result = ""

            loginViewModel.onEmailChange("vespasoft@gmail.com")
            loginViewModel.onPasswordChange("")

            loginViewModel.onSignInClick { param1, param2 ->
                result = param1
            }

            assert(result == expected)
    }

    @Test
    fun `when onEmailChange success uiState should be contentEquals expected`() {
        val expected = "vespasoft@gmail.com"

        loginViewModel.onEmailChange("vespasoft@gmail.com")

        assert(loginViewModel.uiState.value.email contentEquals expected)
    }

    @Test
    fun `when onPasswordChange success uiState should be contentEquals expected`() {
        val expected = "123456"

        loginViewModel.onPasswordChange("123456")

        assert(loginViewModel.uiState.value.password contentEquals expected)
    }

    @Test
    fun `when onForgotPasswordClick email is invalid should not calls to sendRecoveryEmail`() =
        coroutinesTestRule.dispatcher.run {
            loginViewModel.onEmailChange("vespasoft@gm")

            loginViewModel.onForgotPasswordClick()

            coVerify(exactly = 0) { accountRepository.sendRecoveryEmail(any(), any()) }
        }

    @Test
    fun `when onForgotPasswordClick email is ok should calls to sendRecoveryEmail`() =
        coroutinesTestRule.dispatcher.run {
            loginViewModel.onEmailChange("vespasoft@gmail.com")

            coEvery { accountRepository.sendRecoveryEmail(any(), any()) } answers {
                secondArg<(Throwable?) -> Unit>().invoke(null)
            }

            loginViewModel.onForgotPasswordClick()

            coVerify(exactly = 1) { accountRepository.sendRecoveryEmail(any(), any()) }
            coVerify(exactly = 0) { loginViewModel.onError(any()) }
        }

    @Test
    fun `when onForgotPasswordClick throws an exception should calls to onError`() =
        coroutinesTestRule.dispatcher.run {
            loginViewModel.onEmailChange("vespasoft@gmail.com")

            coEvery { accountRepository.sendRecoveryEmail(any(), any()) } answers {
                secondArg<(Throwable?) -> Unit>().invoke(Throwable("Unexpected exception"))
            }

            loginViewModel.onForgotPasswordClick()

            coVerify(exactly = 1) { accountRepository.sendRecoveryEmail(any(), any()) }
            coVerify(exactly = 1) { loginViewModel.onError(any()) }
        }

    @Test
    fun `when onCreateAccountClick email is ok should calls to sendRecoveryEmail`() =
        coroutinesTestRule.dispatcher.run {
            val originExpected = SIGN_UP_SCREEN
            val destinyExpected = LOGIN_SCREEN
            var originResult = ""
            var destinyResult = ""

            loginViewModel.onEmailChange("vespasoft@gmail.com")

            coEvery { accountRepository.sendRecoveryEmail(any(), any()) } answers {
                secondArg<(Throwable?) -> Unit>().invoke(null)
            }

            loginViewModel.onCreateAccountClick { origin, destiny ->
                originResult = origin
                destinyResult = destiny
            }

            assert(originResult == originExpected)
            assert(destinyResult == destinyExpected)

        }
}