package co.be4you.indoorlocalization.app

import android.app.Application
import co.be4you.indoorlocalization.di.modules
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class IndoorLocalizationApp : Application() {
    override fun onCreate() {
        super.onCreate()

        startKoin {
            androidContext(this@IndoorLocalizationApp)
            modules(modules)
        }
    }
}