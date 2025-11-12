package co.be4you.indoorlocalization.viewmodel.registration

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import co.be4you.indoorlocalization.navigation.Route
import co.be4you.indoorlocalization.viewmodel.main.MainAction
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class RegistrationViewModel : ViewModel() {

    private val _state = MutableStateFlow<RegistrationState?>(
        RegistrationState(
            email = "",
            password = "",
            confirmPassword = "",
            isPasswordVisible = false,
            isConfirmPasswordVisible = false,
            isButtonLoading = false,
        )
    )
    val state = _state.asStateFlow()

    private val _event = MutableSharedFlow<MainAction>()
    val event = _event.asSharedFlow()

    fun execute(action: RegistrationAction) {
        when (action) {
            is RegistrationAction.OnConfirmPasswordChanged -> {
                _state.update {
                    it?.copy(confirmPassword = action.newConfirmPassword)
                }
            }

            RegistrationAction.OnConfirmPasswordVisibilityChanged -> {
                _state.update {
                    it?.copy(isConfirmPasswordVisible = it.isConfirmPasswordVisible.not())
                }
            }

            is RegistrationAction.OnEmailChanged -> {
                _state.update {
                    it?.copy(email = action.newEmail)
                }
            }

            is RegistrationAction.OnPasswordChanged -> {
                _state.update {
                    it?.copy(password = action.newPassword)
                }
            }

            RegistrationAction.OnPasswordVisibilityChanged -> {
                _state.update {
                    it?.copy(isPasswordVisible = it.isPasswordVisible.not())
                }
            }

            RegistrationAction.OnRegisterClicked -> {
                // TODO: implement on register clicked
            }

            RegistrationAction.OnLoginClicked -> viewModelScope.launch {
                _event.emit(MainAction.NavigateTo(Route.Login))
            }
        }
    }
}
