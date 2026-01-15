package co.be4you.core.domain.utils.login

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import kotlinx.coroutines.flow.MutableStateFlow

interface LoginHandler<State : LoginUiState, Action : LoginUiAction> {
    var state: MutableStateFlow<State>
    fun execute(action: Action)

    @Composable
    fun LoginLayout(
        modifier: Modifier = Modifier,
    )

    val buttonTextResource: Int
    val titleTextResource: Int
}

