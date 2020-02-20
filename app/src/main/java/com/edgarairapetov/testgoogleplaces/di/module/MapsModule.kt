package com.edgarairapetov.testgoogleplaces.di.module

import androidx.lifecycle.ViewModel
import com.edgarairapetov.testgoogleplaces.di.ViewModelKey
import com.edgarairapetov.testgoogleplaces.viewmodel.MapsViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
abstract class MapsModule {

    @Binds
    @IntoMap
    @ViewModelKey(MapsViewModel::class)
    internal abstract fun mapsViewModel(viewModel: MapsViewModel): ViewModel //todo no used method

}
