package com.demo.instacopy

import android.app.Application
import androidx.appcompat.app.AppCompatDelegate
import timber.log.Timber

/**
 * Created By hrank8t on 3/6/20 - 3:53 PM
 */

@Suppress("unused")
class MyApplication : Application() {
    override fun onCreate() {
        super.onCreate()

        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true)

        Timber.plant(Timber.DebugTree())
    }
}