package com.edgarairapetov.testgoogleplaces.di.module

import android.content.Context
import com.edgarairapetov.testgoogleplaces.app.App
import com.edgarairapetov.testgoogleplaces.utils.GpsUtils
import dagger.Module
import dagger.Provides
import io.reactivex.disposables.CompositeDisposable
import javax.inject.Singleton

@Module
class AppModule(private val application: App) {

    @Provides
    internal fun provideContext(): Context {
        return application.applicationContext
    }

    @Singleton
    @Provides
    internal fun provideDisposable(): CompositeDisposable {
        return CompositeDisposable()
    }

    @Singleton
    @Provides
    internal fun provideGpsUtils(context: Context): GpsUtils {
        return GpsUtils(context)
    }

}
