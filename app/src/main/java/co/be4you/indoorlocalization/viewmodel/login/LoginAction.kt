package co.be4you.indoorlocalization.viewmodel.login

import co.be4you.core.domain.utils.login.LoginHandler

sealed interface LoginAction {
    data class OnLoginHandlerClicked(val loginHandler: LoginHandler<*, *>) : LoginAction
}
