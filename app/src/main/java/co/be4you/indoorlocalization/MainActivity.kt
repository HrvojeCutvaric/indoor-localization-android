package co.be4you.indoorlocalization

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import co.be4you.indoorlocalization.di.viewModelModule
import co.be4you.indoorlocalization.ui.theme.IndoorLocalizationTheme
import co.be4you.indoorlocalization.view.registration.RegistrationScreen
import org.koin.compose.KoinApplication

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            IndoorLocalizationTheme {
                KoinApplication(application = {
                    modules(viewModelModule)
                }) {
                    RegistrationScreen()
                }
            }
        }
    }
}
