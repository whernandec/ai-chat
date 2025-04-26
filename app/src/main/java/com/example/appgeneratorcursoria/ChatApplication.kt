package com.example.appgeneratorcursoria

import android.app.Application
import com.example.appgeneratorcursoria.di.chatModule
import com.example.appgeneratorcursoria.di.networkModule
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin

class ChatApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        
        startKoin {
            androidLogger()
            androidContext(this@ChatApplication)
            modules(
                chatModule,
                networkModule
            )
        }
    }
} 