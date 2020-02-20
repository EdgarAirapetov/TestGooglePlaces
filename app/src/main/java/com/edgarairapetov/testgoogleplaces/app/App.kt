package com.edgarairapetov.testgoogleplaces.app

import android.app.Application
import com.edgarairapetov.testgoogleplaces.di.component.AppComponent
import com.edgarairapetov.testgoogleplaces.di.component.DaggerAppComponent
import com.edgarairapetov.testgoogleplaces.di.module.AppModule

class App : Application() {

    companion object {
        const val BASE_URL = "https://maps.googleapis.com/maps/api/"
        private var component: AppComponent? = null
        private lateinit var app: App

        fun getComponent(): AppComponent {
            return component!!
        }

        fun buildAppComponent() {
            if (component != null)
                component = null

            component = DaggerAppComponent.builder()
                .appModule(AppModule(app))
                .build()
        }

    }

    override fun onCreate() {
        super.onCreate()
        app = this
        buildAppComponent()
    }
}