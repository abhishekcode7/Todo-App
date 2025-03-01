package com.example.tudu

import android.app.Application
import com.example.tudu.util.Utils

class TuduApp:Application() {

    override fun onCreate() {
        super.onCreate()
        Utils.initializeDb(this)
    }
}