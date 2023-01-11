package com.example.amphibiansapp

import android.app.Application
import com.example.amphibiansapp.data.AppContainer
import com.example.amphibiansapp.data.DefaultAppContainer

class AmphibiansApplication : Application() {
    /** AppContainer instance used by the rest of classes to obtain dependencies */
    lateinit var container: AppContainer
    override fun onCreate() {
        super.onCreate()
        container = DefaultAppContainer()
    }
}