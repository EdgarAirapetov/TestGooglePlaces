package com.edgarairapetov.testgoogleplaces

import android.app.Application
import com.edgarairapetov.testgoogleplaces.di.component.AppComponent
import com.edgarairapetov.testgoogleplaces.di.component.DaggerAppComponent
import com.edgarairapetov.testgoogleplaces.di.module.AppModule

class App : Application() {

    companion object {
        private lateinit var component: AppComponent
        private lateinit var app: App

        fun getComponent(): AppComponent {
            return component
        }

        fun buildAppComponent() {
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