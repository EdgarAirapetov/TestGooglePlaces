package com.edgarairapetov.testgoogleplaces.di.component

import com.edgarairapetov.testgoogleplaces.app.App
import com.edgarairapetov.testgoogleplaces.di.module.AppModule
import com.edgarairapetov.testgoogleplaces.di.module.MapsModule
import com.edgarairapetov.testgoogleplaces.di.module.NetworkModule
import com.edgarairapetov.testgoogleplaces.di.module.ViewModelModule
import com.edgarairapetov.testgoogleplaces.support.MapHelper
import com.edgarairapetov.testgoogleplaces.ui.activity.MapsActivity
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = [AppModule::class, NetworkModule::class, ViewModelModule::class, MapsModule::class])
interface AppComponent {
    fun inject(application: App)

    fun inject(activity: MapsActivity)

    fun inject(helper: MapHelper)
}