package com.dadm.appblackdog

import android.app.Application
import com.dadm.appblackdog.database.AppContainer
import com.dadm.appblackdog.database.AppDataContainer

class AppBlackDog: Application() {
    /**
     * AppContainer instance used by the rest of classes to obtain dependencies
     */
    lateinit var container: AppContainer

    override fun onCreate() {
        super.onCreate()
        container = AppDataContainer(this)
    }
}