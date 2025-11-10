package co.be4you.indoorlocalization.view.registration

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import co.be4you.indoorlocalization.R
import co.be4you.indoorlocalization.ui.theme.IndoorLocalizationTheme
import co.be4you.indoorlocalization.view.common.DefaultButton
import co.be4you.indoorlocalization.view.common.DefaultTextField

@Composable
fun RegistrationScreen(

) {

}

@Composable
private fun RegistrationLayout(
    email: String,
    password: String,
    confirmPassword: String,
    isPasswordVisible: Boolean,
    isConfirmPasswordVisible: Boolean,
    onEmailChanged: (String) -> Unit,
    onPasswordChanged: (String) -> Unit,
    onConfirmPasswordChanged: (String) -> Unit,
    onPasswordVisibilityChanged: () -> Unit,
    onConfirmPasswordVisibilityChanged: () -> Unit,
    onRegisterClicked: () -> Unit,
    onLoginClicked: () -> Unit,
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 12.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Text(
            modifier = Modifier.fillMaxWidth(),
            text = stringResource(R.string.registration),
            style = TextStyle(
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary,
            )
        )

        Spacer(modifier = Modifier.height(32.dp))

        DefaultTextField(
            modifier = Modifier.fillMaxWidth(),
            value = email,
            onValueChange = onEmailChanged,
            label = R.string.email,
            placeholder = R.string.email,
        )

        Spacer(modifier = Modifier.height(12.dp))

        DefaultTextField(
            modifier = Modifier.fillMaxWidth(),
            value = password,
            onValueChange = onPasswordChanged,
            label = R.string.password,
            placeholder = R.string.password,
            visualTransformation = if (isPasswordVisible) VisualTransformation.None else PasswordVisualTransformation(),
            trailingIcon = if (isPasswordVisible) R.drawable.ic_visibility_off else R.drawable.ic_visibility,
            onTrailingIconClicked = onPasswordVisibilityChanged,
        )

        Spacer(modifier = Modifier.height(12.dp))

        DefaultTextField(
            modifier = Modifier.fillMaxWidth(),
            value = confirmPassword,
            onValueChange = onConfirmPasswordChanged,
            label = R.string.confirm_password,
            placeholder = R.string.confirm_password,
            visualTransformation = if (isConfirmPasswordVisible) VisualTransformation.None else PasswordVisualTransformation(),
            trailingIcon = if (isConfirmPasswordVisible) R.drawable.ic_visibility_off else R.drawable.ic_visibility,
            onTrailingIconClicked = onConfirmPasswordVisibilityChanged,
        )

        Spacer(modifier = Modifier.height(24.dp))

        DefaultButton(
            modifier = Modifier.fillMaxWidth(),
            label = R.string.register,
            onButtonClicked = onRegisterClicked,
        )

        Spacer(modifier = Modifier.height(12.dp))

        Row(
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Text(
                text = stringResource(R.string.already_have_an_account),
                style = TextStyle(
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Normal,
                    color = MaterialTheme.colorScheme.outline,
                )
            )

            TextButton(
                onClick = onLoginClicked
            ) {
                Text(
                    text = stringResource(R.string.login),
                    style = TextStyle(
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.primary,
                    )
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun RegistrationLayoutPreview() {
    IndoorLocalizationTheme {
        RegistrationLayout(
            email = "test123@gmail.com",
            password = "test123",
            confirmPassword = "test123",
            isPasswordVisible = false,
            isConfirmPasswordVisible = true,
            onEmailChanged = {},
            onPasswordChanged = {},
            onConfirmPasswordChanged = {},
            onPasswordVisibilityChanged = {},
            onConfirmPasswordVisibilityChanged = {},
            onRegisterClicked = {},
            onLoginClicked = {},
        )
    }
}
