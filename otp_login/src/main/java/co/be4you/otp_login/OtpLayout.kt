package co.be4you.otp_login

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import co.be4you.core.ui.components.DefaultButton
import co.be4you.core.ui.components.DefaultTextField
import co.be4you.core.ui.components.LabelWithTextButton
import co.be4you.core.ui.theme.IndoorLocalizationTheme

@Composable
fun OtpLayout(
    state: OtpLoginUiState,
    onAction: (OtpLoginUiAction) -> Unit,
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 12.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Spacer(modifier = Modifier.height(32.dp))

        Text(
            modifier = Modifier.fillMaxWidth(),
            text = stringResource(R.string.sing_in_with_otp),
            style = TextStyle(
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary,
            )
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            modifier = Modifier.fillMaxWidth(),
            text = run {
                state.errorResource?.let {
                    stringResource(it)
                } ?: run {
                    if (state.isOtpSend) stringResource(
                        R.string.enter_the_code_sent_to_your_email,
                        state.email
                    ) else stringResource(R.string.enter_your_email_to_receive_a_one_time_login_code)
                }
            },
            style = TextStyle(
                fontSize = 15.sp,
                fontWeight = FontWeight.Normal,
                color = if (state.errorResource == null) MaterialTheme.colorScheme.outline else MaterialTheme.colorScheme.error,
            ),
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
        )

        Spacer(modifier = Modifier.height(32.dp))

        DefaultTextField(
            modifier = Modifier.fillMaxWidth(),
            value = if (state.isOtpSend) state.code else state.email,
            onValueChange = {
                if (state.isOtpSend) onAction(OtpLoginUiAction.OnCodeChanged(it))
                else onAction(OtpLoginUiAction.OnEmailChanged(it))
            },
            label = if (state.isOtpSend) R.string.code else co.be4you.core.R.string.email,
            placeholder = if (state.isOtpSend) R.string.code else co.be4you.core.R.string.email,
            keyboardOptions = if (state.isOtpSend) KeyboardOptions(keyboardType = KeyboardType.Number) else KeyboardOptions.Default,
        )

        Spacer(modifier = Modifier.height(32.dp))

        DefaultButton(
            modifier = Modifier.fillMaxWidth(),
            label = if (state.isOtpSend.not()) R.string.send_otp_code else co.be4you.core.R.string.login,
            onButtonClicked = {
                if (state.isOtpSend.not()) onAction(OtpLoginUiAction.OnRequestOtpClicked)
                else onAction(OtpLoginUiAction.OnVerifyOtpClicked)
            },
            isButtonLoading = state.isButtonLoading,
            isButtonEnabled = state.isButtonLoading.not(),
        )

        Spacer(modifier = Modifier.height(12.dp))

        LabelWithTextButton(
            label = stringResource(co.be4you.core.R.string.dont_have_an_account),
            buttonLabel = stringResource(co.be4you.core.R.string.registration),
            isButtonLoading = state.isButtonLoading,
            onTextButtonClicked = { onAction(OtpLoginUiAction.OnRegisterClicked) },
        )

        if (state.isOtpSend) {
            LabelWithTextButton(
                label = stringResource(R.string.wrong_email),
                buttonLabel = stringResource(R.string.change),
                isButtonLoading = state.isButtonLoading,
                onTextButtonClicked = { onAction(OtpLoginUiAction.OnChangeEmailClicked) },
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun PasswordLayoutPreview() {
    IndoorLocalizationTheme {
        OtpLayout(
            state = OtpLoginUiState(
                email = "hcutvaric@gmail.com",
                code = "",
                isOtpSend = true,
                errorResource = null,
                isButtonLoading = false,
            ),
            onAction = {},
        )
    }
}
