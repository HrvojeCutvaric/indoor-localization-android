package co.be4you.indoorlocalization

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.ui.Modifier
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.ui.NavDisplay
import co.be4you.core.navigation.AppNavigator
import co.be4you.core.navigation.Route
import co.be4you.core.ui.theme.IndoorLocalizationTheme
import co.be4you.indoorlocalization.view.assets.AssetsScreen
import co.be4you.indoorlocalization.view.dashboard.DashboardScreen
import co.be4you.indoorlocalization.view.login.LoginScreen
import co.be4you.indoorlocalization.view.registration.RegistrationScreen
import co.be4you.indoorlocalization.viewmodel.main.MainViewModel
import org.koin.androidx.compose.koinViewModel
import org.koin.compose.koinInject

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val mainViewModel = koinViewModel<MainViewModel>()
            val appNavigator = koinInject<AppNavigator>()
            IndoorLocalizationTheme {
                NavDisplay(
                    modifier = Modifier
                        .statusBarsPadding()
                        .navigationBarsPadding(),
                    backStack = appNavigator.backStack,
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
                            DashboardScreen(onAction = mainViewModel::execute)
                        }

                            entry<Route.Assets> { assets ->
                                AssetsScreen(
                                    floorMapId = assets.floorMapId,
                                    floorMapName = assets.floorMapName,
                                    onAction = mainViewModel::execute
                                )
                            }
                        }
                    )
                }
            }
        }
    }

