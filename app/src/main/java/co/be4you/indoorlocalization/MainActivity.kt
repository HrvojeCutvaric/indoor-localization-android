package co.be4you.indoorlocalization

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.ui.NavDisplay
import co.be4you.indoorlocalization.di.modules
import co.be4you.indoorlocalization.navigation.Route
import co.be4you.indoorlocalization.ui.theme.IndoorLocalizationTheme
import co.be4you.indoorlocalization.view.dashboard.DashboardScreen
import co.be4you.indoorlocalization.view.login.LoginScreen
import co.be4you.indoorlocalization.view.registration.RegistrationScreen
import co.be4you.indoorlocalization.viewmodel.main.MainViewModel
import org.koin.androidx.compose.koinViewModel
import org.koin.compose.KoinApplication

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            KoinApplication(application = {
                modules(modules)
            }) {
                val mainViewModel = koinViewModel<MainViewModel>()

                IndoorLocalizationTheme {
                    NavDisplay(
                        backStack = mainViewModel.backStack,
                        transitionSpec = {
                            fadeIn(tween(300)) togetherWith fadeOut(tween(300))
                        },
                        entryProvider = entryProvider {
                            entry<Route.Registration> {
                                RegistrationScreen(onAction = mainViewModel::execute)
                            }

                            entry<Route.Login> {
                                LoginScreen(onAction = mainViewModel::execute)
                            }

                            entry<Route.Dashboard> {
                                DashboardScreen()
                            }
                        }
                    )
                }
            }
        }
    }
}
