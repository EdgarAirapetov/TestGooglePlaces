package com.edgarairapetov.testgoogleplaces.di.module

import android.content.Context
import android.content.res.Resources
import android.location.LocationManager
import androidx.appcompat.app.AlertDialog
import com.edgarairapetov.testgoogleplaces.App
import com.edgarairapetov.testgoogleplaces.common.GpsHelper
import com.edgarairapetov.testgoogleplaces.common.LocationUtils
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.LocationSettingsRequest
import com.google.android.gms.location.SettingsClient
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
    internal fun provideResources(context: Context): Resources {
        return context.resources
    }

    @Singleton
    @Provides
    internal fun provideDisposable(): CompositeDisposable {
        return CompositeDisposable()
    }

    @Singleton
    @Provides
    internal fun provideSettingClient(context: Context): SettingsClient {
        return LocationServices.getSettingsClient(context)
    }

    @Singleton
    @Provides
    internal fun provideLocManager(context: Context): LocationManager {
        return context.getSystemService(Context.LOCATION_SERVICE) as LocationManager
    }

    @Singleton
    @Provides
    internal fun provideGpsUtils(context: Context, resources: Resources, settingsClient: SettingsClient, locationManager: LocationManager): GpsHelper {
        return GpsHelper(context, resources, settingsClient, locationManager)
    }
}
