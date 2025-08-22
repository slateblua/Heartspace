package com.bluesourceplus.heartspace

import android.app.Application
import com.bluesourceplus.heartspace.data.database.module.dataModule
import com.bluesourceplus.heartspace.feature.aboutmoodentry.module.aboutModule
import com.bluesourceplus.heartspace.feature.create.module.createModule
import com.bluesourceplus.heartspace.feature.home.module.homeModule
import com.bluesourceplus.heartspace.feature.preferences.module.preferencesModule
import com.bluesourceplus.heartspace.feature.reflect.reflectModule
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin

class HeartspaceApp : Application() {
    override fun onCreate() {
        super.onCreate()

        startKoin {
            androidContext(this@HeartspaceApp)
            androidLogger()
            modules(
                homeModule,
                reflectModule,
                dataModule,
                createModule,
                preferencesModule,
                aboutModule,
            )
        }
    }
}