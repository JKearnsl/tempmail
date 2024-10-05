package com.jkearnsl.tempmail

import android.app.Application

class TempMail: Application() {
    lateinit var core: Core

    override fun onCreate() {
        super.onCreate()
        core = Core(getSharedPreferences("context", MODE_PRIVATE))
    }
}
