package com.mika.pea

import android.app.Application
import com.mika.pea.data.AppContainer
import com.mika.pea.data.AppContainerImpl

class MikaPeaApp : Application() {
    lateinit var container: AppContainer
        private set

    override fun onCreate() {
        super.onCreate()
        container = AppContainerImpl(this)
    }
}
