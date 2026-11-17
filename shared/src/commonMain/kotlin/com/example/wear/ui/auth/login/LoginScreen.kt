package com.example.wear.ui.auth.login

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.wear.core.mvi.ObserveEffects
import com.example.wear.ui.auth.components.AuthBrand
import com.example.wear.ui.auth.components.AuthGreeting
import com.example.wear.ui.auth.components.AuthPrimaryButton
import com.example.wear.ui.auth.components.AuthScaffold
import com.example.wear.ui.auth.components.AuthSheetTitle
import com.example.wear.ui.auth.components.AuthTextField
import com.example.wear.ui.auth.components.PasswordVisibilityToggle
import com.example.wear.ui.auth.components.authErrorColor
import com.example.wear.ui.auth.components.message
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.viewmodel.koinViewModel
import wear.shared.generated.resources.*

@Composable
fun LoginRoute(
    registeredUsername: String?,
    onSignedIn: () -> Unit,
    onOpenRegister: () -> Unit,
    viewModel: LoginViewModel = koinViewModel()
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    LaunchedEffect(registeredUsername) {
        if (registeredUsername != null) viewModel.onIntent(LoginIntent.PrefillAfterRegistration(registeredUsername))
    }
    ObserveEffects(viewModel.effects) { effect ->
        when (effect) {
            LoginEffect.SignedIn -> onSignedIn()
            LoginEffect.OpenRegister -> onOpenRegister()
        }
    }
    LoginScreen(state = state, onIntent = viewModel::onIntent)
}

@Composable
fun LoginScreen(
    state: LoginState,
    onIntent: (LoginIntent) -> Unit,
    modifier: Modifier = Modifier
) {
    val passwordFocus = remember { FocusRequester() }
    val focusManager = LocalFocusManager.current

    AuthScaffold(
        modifier = modifier,
        header = {
            AuthBrand()
            AuthGreeting(
                title = stringResource(Res.string.login_greeting),
                subtitle = stringResource(Res.string.login_greeting_sub)
            )
        },
        sheet = {
            AuthSheetTitle(stringResource(Res.string.login_title))

            if (state.justRegistered) {
                Text(
                    stringResource(Res.string.login_registered_notice),
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSecondaryContainer,
                    textAlign = TextAlign.Center,
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(MaterialTheme.shapes.medium)
                        .background(MaterialTheme.colorScheme.secondaryContainer)
                        .padding(12.dp)
                )
            }

            // Клавіатура, поле імені: дія «Далі» переводить фокус на поле пароля
            AuthTextField(
                value = state.username,
                onValueChange = { onIntent(LoginIntent.UsernameChanged(it)) },
                placeholder = stringResource(Res.string.auth_username_label),
                leadingIcon = Res.drawable.ic_user,
                errorText = state.usernameError?.message(),
                enabled = !state.isLoading,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text, imeAction = ImeAction.Next),
                keyboardActions = KeyboardActions(onNext = { passwordFocus.requestFocus() })
            )

            // Клавіатура, поле пароля: дія «Готово» виконує вхід (так само, як кнопка «Увійти»)
            AuthTextField(
                value = state.password,
                onValueChange = { onIntent(LoginIntent.PasswordChanged(it)) },
                placeholder = stringResource(Res.string.auth_password_label),
                leadingIcon = Res.drawable.ic_lock,
                modifier = Modifier.focusRequester(passwordFocus),
                errorText = state.passwordError?.message(),
                enabled = !state.isLoading,
                visualTransformation = if (state.passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password, imeAction = ImeAction.Done),
                keyboardActions = KeyboardActions(onDone = {
                    focusManager.clearFocus()
                    onIntent(LoginIntent.Submit)
                }),
                trailing = {
                    PasswordVisibilityToggle(
                        visible = state.passwordVisible,
                        onToggle = { onIntent(LoginIntent.TogglePasswordVisibility) }
                    )
                }
            )

            state.authError?.let {
                Text(it.message(), style = MaterialTheme.typography.bodySmall, color = authErrorColor())
            }

            AuthPrimaryButton(
                text = stringResource(Res.string.login_button),
                isLoading = state.isLoading,
                onClick = {
                    focusManager.clearFocus()
                    onIntent(LoginIntent.Submit)
                }
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    stringResource(Res.string.login_no_account),
                    modifier = Modifier.weight(1f, fill = false),
                    textAlign = TextAlign.End,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                TextButton(onClick = { onIntent(LoginIntent.OpenRegister) }, enabled = !state.isLoading) {
                    Text(stringResource(Res.string.login_create_account))
                }
            }
        }
    )
}
