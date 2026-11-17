package com.example.wear.ui.auth.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.WindowInsetsSides
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.only
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.luminance
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import com.example.wear.domain.model.AuthError
import com.example.wear.domain.model.ValidationError
import com.example.wear.ui.theme.ErrorRed
import com.example.wear.ui.theme.ErrorRedDark
import com.example.wear.ui.theme.SkyBlue
import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import wear.shared.generated.resources.*

/** Колір шапки в темній темі: глибокий синій, узгоджений з палітрою. */
private val HeaderDark = Color(0xFF1B3A5E)

/** Дуже світлий блакитний відтінок білого для нижньої панелі (світла тема). */
private val SheetLight = Color(0xFFF4F9FE)

private val SheetShape = RoundedCornerShape(topStart = 40.dp, topEnd = 40.dp)

private fun isDark(background: Color): Boolean = background.luminance() < 0.5f

/** Колір тексту помилок: контрастний і на світлій, і на темній темі. */
@Composable
fun authErrorColor(): Color =
    if (isDark(MaterialTheme.colorScheme.background)) ErrorRedDark else ErrorRed

/**
 * Каркас екрана авторизації: блакитна шапка зверху ([header]) і біла панель із заокругленими верхніми
 * кутами ([sheet]) на решту екрана. Панель прокручується, тому клавіатура не перекриває поля.
 * На широких екранах (desktop) вміст обмежений за шириною й стоїть по центру.
 */
@Composable
fun AuthScaffold(
    modifier: Modifier = Modifier,
    header: @Composable ColumnScope.() -> Unit,
    sheet: @Composable ColumnScope.() -> Unit
) {
    val dark = isDark(MaterialTheme.colorScheme.background)
    Box(
        modifier = modifier
            .fillMaxSize()
            .background(if (dark) HeaderDark else SkyBlue),
        contentAlignment = Alignment.TopCenter
    ) {
        Column(Modifier.widthIn(max = 480.dp).fillMaxSize()) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .windowInsetsPadding(WindowInsets.safeDrawing.only(WindowInsetsSides.Top + WindowInsetsSides.Horizontal))
                    .padding(start = 28.dp, end = 28.dp, top = 16.dp, bottom = 28.dp),
                content = header
            )
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
                    .clip(SheetShape)
                    .background(if (dark) MaterialTheme.colorScheme.surface else SheetLight)
                    .windowInsetsPadding(WindowInsets.safeDrawing.only(WindowInsetsSides.Horizontal + WindowInsetsSides.Bottom))
                    .verticalScroll(rememberScrollState())
                    .padding(start = 28.dp, end = 28.dp, top = 32.dp, bottom = 32.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp),
                content = sheet
            )
        }
    }
}

/** Логотип і назва застосунку: зліва вгорі шапки. Власний логотип: див. BrandLogo.kt. */
@Composable
fun AuthBrand(modifier: Modifier = Modifier) {
    Row(modifier = modifier, verticalAlignment = Alignment.CenterVertically) {
        Box(
            modifier = Modifier
                .size(44.dp)
                .clip(CircleShape)
                .background(MaterialTheme.colorScheme.primary),
            contentAlignment = Alignment.Center
        ) {
            val logo = BrandLogo
            if (logo != null) {
                Image(
                    painter = painterResource(logo),
                    contentDescription = null,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.fillMaxSize()
                )
            } else {
                Icon(
                    painter = painterResource(Res.drawable.ic_wardrobe),
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.onPrimary,
                    modifier = Modifier.size(26.dp)
                )
            }
        }
        Spacer(Modifier.width(12.dp))
        Text(
            stringResource(Res.string.brand_name),
            style = MaterialTheme.typography.headlineSmall,
            color = MaterialTheme.colorScheme.onPrimaryContainer
        )
    }
}

/** Велике привітання під логотипом у шапці. */
@Composable
fun AuthGreeting(title: String, subtitle: String, modifier: Modifier = Modifier) {
    Column(modifier = modifier.padding(top = 28.dp), verticalArrangement = Arrangement.spacedBy(4.dp)) {
        Text(
            title,
            style = MaterialTheme.typography.displaySmall,
            color = MaterialTheme.colorScheme.onPrimaryContainer
        )
        Text(
            subtitle,
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}

/** Заголовок панелі: «Вхід» / «Реєстрація». */
@Composable
fun AuthSheetTitle(text: String) {
    Text(
        text,
        style = MaterialTheme.typography.headlineMedium,
        color = MaterialTheme.colorScheme.onPrimaryContainer
    )
}

/**
 * Поле вводу: біла «картка» з м'якою тінню, іконка ліворуч, підказка-плейсхолдер.
 * Помилка підсвічує рамку й показується текстом під полем.
 */
@Composable
fun AuthTextField(
    value: String,
    onValueChange: (String) -> Unit,
    placeholder: String,
    leadingIcon: DrawableResource,
    modifier: Modifier = Modifier,
    errorText: String? = null,
    hintText: String? = null,
    enabled: Boolean = true,
    visualTransformation: VisualTransformation = VisualTransformation.None,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    keyboardActions: KeyboardActions = KeyboardActions.Default,
    trailing: (@Composable () -> Unit)? = null
) {
    val scheme = MaterialTheme.colorScheme
    val dark = isDark(scheme.background)
    val error = authErrorColor()
    val shape = MaterialTheme.shapes.medium
    val supporting = errorText ?: hintText

    Column(modifier = modifier.fillMaxWidth(), verticalArrangement = Arrangement.spacedBy(6.dp)) {
        Surface(
            shape = shape,
            color = if (dark) scheme.surfaceVariant else Color.White,
            shadowElevation = 6.dp,
            border = if (errorText != null) BorderStroke(1.dp, error) else null
        ) {
            TextField(
                value = value,
                onValueChange = onValueChange,
                modifier = Modifier.fillMaxWidth(),
                enabled = enabled,
                singleLine = true,
                isError = errorText != null,
                shape = shape,
                placeholder = { Text(placeholder) },
                leadingIcon = {
                    Icon(painterResource(leadingIcon), contentDescription = null, modifier = Modifier.size(22.dp))
                },
                trailingIcon = trailing,
                visualTransformation = visualTransformation,
                keyboardOptions = keyboardOptions,
                keyboardActions = keyboardActions,
                colors = TextFieldDefaults.colors(
                    focusedContainerColor = Color.Transparent,
                    unfocusedContainerColor = Color.Transparent,
                    disabledContainerColor = Color.Transparent,
                    errorContainerColor = Color.Transparent,
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent,
                    disabledIndicatorColor = Color.Transparent,
                    errorIndicatorColor = Color.Transparent,
                    cursorColor = scheme.primary,
                    errorCursorColor = error,
                    focusedLeadingIconColor = scheme.primary,
                    unfocusedLeadingIconColor = scheme.onSurfaceVariant,
                    errorLeadingIconColor = error,
                    focusedPlaceholderColor = scheme.onSurfaceVariant,
                    unfocusedPlaceholderColor = scheme.onSurfaceVariant,
                    errorPlaceholderColor = scheme.onSurfaceVariant
                )
            )
        }
        if (supporting != null) {
            Text(
                supporting,
                style = MaterialTheme.typography.labelSmall,
                color = if (errorText != null) error else scheme.onSurfaceVariant,
                modifier = Modifier.padding(horizontal = 16.dp)
            )
        }
    }
}

/** Кнопка показати/сховати пароль (значок ока). */
@Composable
fun PasswordVisibilityToggle(visible: Boolean, onToggle: () -> Unit) {
    IconButton(onClick = onToggle) {
        Icon(
            painter = painterResource(if (visible) Res.drawable.ic_eye_off else Res.drawable.ic_eye),
            contentDescription = stringResource(if (visible) Res.string.auth_hide else Res.string.auth_show),
            modifier = Modifier.size(22.dp)
        )
    }
}

/** Головна кнопка: темно-синя, закруглена; під час запиту показує індикатор і не реагує на натискання. */
@Composable
fun AuthPrimaryButton(
    text: String,
    onClick: () -> Unit,
    isLoading: Boolean,
    modifier: Modifier = Modifier
) {
    val scheme = MaterialTheme.colorScheme
    Button(
        onClick = onClick,
        enabled = !isLoading,
        shape = MaterialTheme.shapes.medium,
        colors = ButtonDefaults.buttonColors(
            containerColor = scheme.secondary,
            contentColor = scheme.onSecondary,
            disabledContainerColor = scheme.secondary,
            disabledContentColor = scheme.onSecondary
        ),
        modifier = modifier.fillMaxWidth().height(52.dp)
    ) {
        if (isLoading) {
            CircularProgressIndicator(
                modifier = Modifier.size(22.dp),
                strokeWidth = 2.5.dp,
                color = scheme.onSecondary
            )
        } else {
            Text(text, style = MaterialTheme.typography.labelLarge)
        }
    }
}

@Composable
fun ValidationError.message(): String = stringResource(
    when (this) {
        ValidationError.UsernameEmpty -> Res.string.error_username_empty
        ValidationError.UsernameTooShort -> Res.string.error_username_short
        ValidationError.UsernameTooLong -> Res.string.error_username_long
        ValidationError.UsernameInvalidChars -> Res.string.error_username_chars
        ValidationError.PasswordEmpty -> Res.string.error_password_empty
        ValidationError.PasswordTooShort -> Res.string.error_password_short
        ValidationError.PasswordTooLong -> Res.string.error_password_long
        ValidationError.PasswordNeedsLetterAndDigit -> Res.string.error_password_weak
        ValidationError.ConfirmationEmpty -> Res.string.error_confirmation_empty
        ValidationError.PasswordsDoNotMatch -> Res.string.error_password_mismatch
    }
)

@Composable
fun AuthError.message(): String = stringResource(
    when (this) {
        AuthError.InvalidCredentials -> Res.string.error_invalid_credentials
        AuthError.UserAlreadyExists -> Res.string.error_user_exists
    }
)
