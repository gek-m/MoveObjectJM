package com.interview.movecarjm

import android.app.Application
import com.interview.movecarjm.di.Modules
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class App: Application() {
    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidContext(this@App)
            modules(
                Modules.mainPresenterModule
            )
        }
    }
}