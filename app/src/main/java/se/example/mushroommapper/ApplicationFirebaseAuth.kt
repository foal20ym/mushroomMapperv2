package se.example.mushroommapper

import android.app.Application
import dagger.hilt.android.HiltAndroidApp
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.GlobalContext
import org.koin.core.logger.Level
import se.example.mushroommapper.dependencyInjections.appModule


@HiltAndroidApp
class ApplicationFirebaseAuth: Application() {

    override fun onCreate() {
        super.onCreate()

        GlobalContext.startKoin {
            androidLogger(if (BuildConfig.DEBUG) Level.ERROR else Level.NONE)
            androidContext(this@ApplicationFirebaseAuth)
            modules(appModule)
        }
    }

}