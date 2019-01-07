package com.camillebc.fusy

import android.app.Application
import com.camillebc.fusy.di.DaggerFictionComponent
import com.camillebc.fusy.di.FictionComponent
import com.camillebc.fusy.di.modules.AppModule
import com.camillebc.fusy.di.modules.HardwareStatusModule

class FusyApplication: Application() {
    lateinit var fictionComponent: FictionComponent
        private set

    override fun onCreate() {
        super.onCreate()
        INSTANCE = this
        fictionComponent = DaggerFictionComponent.builder()
            .appModule(AppModule(this))
            .build()
    }

    companion object {
        private var INSTANCE: FusyApplication? = null

        @JvmStatic
        fun get(): FusyApplication = INSTANCE!!
    }
}