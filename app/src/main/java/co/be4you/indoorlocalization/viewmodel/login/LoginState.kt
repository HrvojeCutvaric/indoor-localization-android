package co.be4you.indoorlocalization.viewmodel.login

data class LoginState(
    val username: String,
    val password: String,
    val isPasswordVisible: Boolean,
    val errorResource: Int?,
    val isButtonLoading: Boolean,
)
