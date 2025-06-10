package com.ml.shubham0204.docease

import android.app.Application
import com.ml.shubham0204.docease.data.ObjectBoxStore
import com.ml.shubham0204.docease.di.AppModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin
import org.koin.ksp.generated.module

class DocEaseApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidContext(this@DocEaseApplication)
            modules(AppModule().module)
        }
        ObjectBoxStore.init(this)
    }
}
