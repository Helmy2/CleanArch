package com.example.feature.auth.presentation

import android.content.Context
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performTextInput
import androidx.test.platform.app.InstrumentationRegistry
import com.example.feature.auth.R
import io.kotest.matchers.collections.shouldContain
import io.kotest.matchers.shouldBe
import org.junit.Before
import org.junit.Rule
import org.junit.Test


class AuthScreenTest {

    @get:Rule
    val composeTestRule = createComposeRule()
    private lateinit var viewModel: FakeAuthViewModel
    private lateinit var context: Context

    @Before
    fun setup() {
        context = InstrumentationRegistry.getInstrumentation().targetContext
        viewModel = FakeAuthViewModel()
    }

    @Test
    fun initialState_shouldShowLoginForm() {
        composeTestRule.setContent {
            AuthScreen(
                state = viewModel.state.value,
                onEvent = viewModel::handleEvent
            )
        }

        composeTestRule.apply {
            onNodeWithText(context.getString(R.string.welcome_back)).assertExists()
            onNodeWithText(context.getString(R.string.login_to_your_account)).assertExists()
            onNodeWithText(context.getString(R.string.email)).assertExists()
            onNodeWithText(context.getString(R.string.password)).assertExists()
            onNodeWithText(context.getString(R.string.login)).assertExists()
            onNodeWithText(context.getString(R.string.forgot_password)).assertExists()
            onNodeWithText(context.getString(R.string.continue_as_guest)).assertExists()
            onNodeWithText(context.getString(R.string.no_account)).assertExists()
        }
    }

    @Test
    fun emailInput_shouldUpdateState() {
        composeTestRule.setContent {
            AuthScreen(
                state = viewModel.state.value,
                onEvent = viewModel::handleEvent
            )
        }

        val testEmail = "test@example.com"
        composeTestRule.onNodeWithText(context.getString(R.string.email))
            .performTextInput(testEmail)

        viewModel.state.value.email shouldBe testEmail
        viewModel.events shouldContain AuthEvent.EmailChanged(testEmail)
    }

    @Test
    fun invalidEmail_shouldShowError() {
        viewModel.setErrorState(emailError = R.string.error_invalid_email)

        composeTestRule.setContent {
            AuthScreen(
                state = viewModel.state.value,
                onEvent = viewModel::handleEvent
            )
        }

        composeTestRule.onNodeWithText(context.getString(R.string.error_invalid_email))
            .assertExists()
    }

    @Test
    fun passwordVisibilityToggle_shouldUpdateState() {
        composeTestRule.setContent {
            AuthScreen(
                state = viewModel.state.value,
                onEvent = viewModel::handleEvent
            )
        }

        val initialVisibility = viewModel.state.value.isPasswordVisible
        composeTestRule.onNodeWithContentDescription(context.getString(R.string.show_password))
            .performClick()

        viewModel.state.value.isPasswordVisible shouldBe !initialVisibility
        viewModel.events shouldContain AuthEvent.TogglePasswordVisibility
    }

    @Test
    fun authModeSwitch_shouldUpdateUI() {
        composeTestRule.setContent {
            AuthScreen(
                state = viewModel.state.value,
                onEvent = viewModel::handleEvent
            )
        }

        // Switch to Register mode
        composeTestRule.onNodeWithText(context.getString(R.string.no_account))
            .performClick()

        viewModel.state.value.authMode shouldBe AuthMode.Register
        composeTestRule.onNodeWithText(context.getString(R.string.name)).assertExists()

        // Switch back to Login
        composeTestRule.onNodeWithText(context.getString(R.string.already_have_account))
            .performClick()

        viewModel.state.value.authMode shouldBe AuthMode.Login
        composeTestRule.onNodeWithText(context.getString(R.string.name)).assertDoesNotExist()
    }

    @Test
    fun authButtonClick_shouldShowLoadingState() {
        composeTestRule.setContent {
            AuthScreen(
                state = viewModel.state.value,
                onEvent = viewModel::handleEvent
            )
        }

        composeTestRule.onNodeWithText(context.getString(R.string.login))
            .performClick()

        viewModel.state.value.isLoading shouldBe true
    }

    @Test
    fun anonymousSignIn_shouldTriggerEvent() {
        composeTestRule.setContent {
            AuthScreen(
                state = viewModel.state.value,
                onEvent = viewModel::handleEvent
            )
        }

        composeTestRule.onNodeWithText(context.getString(R.string.continue_as_guest))
            .performClick()

        viewModel.events shouldContain AuthEvent.SignInAnonymously
        viewModel.state.value.isLoading shouldBe true
    }

    @Test
    fun forgotPasswordFlow_shouldWorkCorrectly() {
        composeTestRule.setContent {
            AuthScreen(
                state = viewModel.state.value,
                onEvent = viewModel::handleEvent
            )
        }

        // Go to forgot password
        composeTestRule.onNodeWithText(context.getString(R.string.forgot_password))
            .performClick()

        viewModel.state.value.authMode shouldBe AuthMode.ForgotPassword
        composeTestRule.onNodeWithText(context.getString(R.string.reset_password)).assertExists()

        // Enter email and submit
        val testEmail = "recovery@example.com"
        composeTestRule.onNodeWithText(context.getString(R.string.email))
            .performTextInput(testEmail)

        composeTestRule.onNodeWithText(context.getString(R.string.reset_password))
            .performClick()

        viewModel.events shouldContain AuthEvent.EmailChanged(testEmail)
        viewModel.events shouldContain AuthEvent.AuthButtonClicked
    }
}