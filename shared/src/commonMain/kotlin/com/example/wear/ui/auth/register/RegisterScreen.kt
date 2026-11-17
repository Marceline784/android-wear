package com.example.wear.ui.auth.register

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.wear.core.mvi.ObserveEffects
import com.example.wear.ui.auth.components.AuthBrand
import com.example.wear.ui.auth.components.AuthPrimaryButton
import com.example.wear.ui.auth.components.AuthScaffold
import com.example.wear.ui.auth.components.AuthSheetTitle
import com.example.wear.ui.auth.components.AuthTextField
import com.example.wear.ui.auth.components.PasswordVisibilityToggle
import com.example.wear.ui.auth.components.authErrorColor
import com.example.wear.ui.auth.components.message
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.viewmodel.koinViewModel
import wear.shared.generated.resources.*

@Composable
fun RegisterRoute(
    onRegistered: (String) -> Unit,
    onOpenLogin: () -> Unit,
    viewModel: RegisterViewModel = koinViewModel()
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    ObserveEffects(viewModel.effects) { effect ->
        when (effect) {
            is RegisterEffect.Registered -> onRegistered(effect.username)
            RegisterEffect.OpenLogin -> onOpenLogin()
        }
    }
    RegisterScreen(state = state, onIntent = viewModel::onIntent)
}

@Composable
fun RegisterScreen(
    state: RegisterState,
    onIntent: (RegisterIntent) -> Unit,
    modifier: Modifier = Modifier
) {
    val passwordFocus = remember { FocusRequester() }
    val confirmationFocus = remember { FocusRequester() }
    val focusManager = LocalFocusManager.current
    val passwordTransformation =
        if (state.passwordVisible) VisualTransformation.None else PasswordVisualTransformation()

    AuthScaffold(
        modifier = modifier,
        header = { AuthBrand() },
        sheet = {
            // «Назад до входу»
            TextButton(
                onClick = { onIntent(RegisterIntent.OpenLogin) },
                enabled = !state.isLoading,
                contentPadding = PaddingValues(0.dp)
            ) {
                Icon(painterResource(Res.drawable.ic_arrow_back), contentDescription = null, modifier = Modifier.size(20.dp))
                Spacer(Modifier.width(8.dp))
                Text(stringResource(Res.string.register_back), style = MaterialTheme.typography.labelLarge)
            }

            AuthSheetTitle(stringResource(Res.string.register_title))

            // Ім'я -> «Далі» -> пароль
            AuthTextField(
                value = state.username,
                onValueChange = { onIntent(RegisterIntent.UsernameChanged(it)) },
                placeholder = stringResource(Res.string.auth_username_label),
                leadingIcon = Res.drawable.ic_user,
                errorText = state.usernameError?.message(),
                hintText = stringResource(Res.string.register_username_hint),
                enabled = !state.isLoading,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text, imeAction = ImeAction.Next),
                keyboardActions = KeyboardActions(onNext = { passwordFocus.requestFocus() })
            )

            // Пароль -> «Далі» -> підтвердження
            AuthTextField(
                value = state.password,
                onValueChange = { onIntent(RegisterIntent.PasswordChanged(it)) },
                placeholder = stringResource(Res.string.auth_password_label),
                leadingIcon = Res.drawable.ic_lock,
                modifier = Modifier.focusRequester(passwordFocus),
                errorText = state.passwordError?.message(),
                hintText = stringResource(Res.string.register_password_hint),
                enabled = !state.isLoading,
                visualTransformation = passwordTransformation,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password, imeAction = ImeAction.Next),
                keyboardActions = KeyboardActions(onNext = { confirmationFocus.requestFocus() }),
                trailing = {
                    PasswordVisibilityToggle(
                        visible = state.passwordVisible,
                        onToggle = { onIntent(RegisterIntent.TogglePasswordVisibility) }
                    )
                }
            )

            // Підтвердження -> «Готово» -> реєстрація
            AuthTextField(
                value = state.confirmation,
                onValueChange = { onIntent(RegisterIntent.ConfirmationChanged(it)) },
                placeholder = stringResource(Res.string.auth_confirm_password_label),
                leadingIcon = Res.drawable.ic_lock,
                modifier = Modifier.focusRequester(confirmationFocus),
                errorText = state.confirmationError?.message(),
                enabled = !state.isLoading,
                visualTransformation = passwordTransformation,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password, imeAction = ImeAction.Done),
                keyboardActions = KeyboardActions(onDone = {
                    focusManager.clearFocus()
                    onIntent(RegisterIntent.Submit)
                })
            )

            state.authError?.let {
                Text(it.message(), style = MaterialTheme.typography.bodySmall, color = authErrorColor())
            }

            AuthPrimaryButton(
                text = stringResource(Res.string.register_button),
                isLoading = state.isLoading,
                onClick = {
                    focusManager.clearFocus()
                    onIntent(RegisterIntent.Submit)
                }
            )
        }
    )
}
