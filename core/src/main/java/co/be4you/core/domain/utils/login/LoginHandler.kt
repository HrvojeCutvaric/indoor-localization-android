package co.be4you.core.domain.utils.login

import androidx.compose.runtime.Composable
import kotlinx.coroutines.flow.MutableStateFlow

interface LoginHandler<State : LoginUiState, Action : LoginUiAction> {
    var state: MutableStateFlow<State>
    fun execute(action: Action)

    @Composable
    fun LoginLayout()

    val buttonTextResource: Int
}

