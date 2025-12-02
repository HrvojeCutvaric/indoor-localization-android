package co.be4you.indoorlocalization

import android.app.Application
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin
import co.be4you.indoorlocalization.di.modules

class IndoorLocalizationApp : Application() {
    override fun onCreate() {
        super.onCreate()

        startKoin {
            androidContext(this@IndoorLocalizationApp)
            modules(modules)
        }
    }
}
