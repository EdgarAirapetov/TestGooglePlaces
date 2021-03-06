package com.edgarairapetov.testgoogleplaces.di.module

import androidx.lifecycle.ViewModelProvider
import com.edgarairapetov.testgoogleplaces.common.ViewModelFactory
import dagger.Binds
import dagger.Module

@Module
abstract class ViewModelModule {
    @Binds
    internal abstract fun bindViewModelFactory(factory: ViewModelFactory): ViewModelProvider.Factory
}