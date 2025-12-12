package co.be4you.indoorlocalization.viewmodel.login

import co.be4you.core.domain.utils.login.LoginHandler

data class LoginState(
    val loginHandlers: List<LoginHandler<*, *>>,
    val loginHandler: LoginHandler<*, *>?,
)
