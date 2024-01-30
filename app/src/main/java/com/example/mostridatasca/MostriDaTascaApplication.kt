package com.example.mostridatasca

import android.app.Application
import com.example.mostridatasca.data.AppContainer
import com.example.mostridatasca.data.AppDataContainer

class MostriDaTascaApplication() : Application() {
    lateinit var container: AppContainer

    override fun onCreate() {
        super.onCreate()
        container = AppDataContainer(this)
    }
}