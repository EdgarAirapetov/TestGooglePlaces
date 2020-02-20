package com.edgarairapetov.testgoogleplaces.di.module

import android.content.Context
import com.edgarairapetov.testgoogleplaces.api.ApiService
import com.edgarairapetov.testgoogleplaces.api.interceptor.RequestInterceptor
import com.edgarairapetov.testgoogleplaces.app.App.Companion.BASE_URL
import dagger.Module
import dagger.Provides
import okhttp3.Cache
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module
class NetworkModule {

    @Provides
    @Singleton
    fun provideInterceptor(): RequestInterceptor {
        return RequestInterceptor()
    }

    @Provides
    @Singleton
    fun provideOkHttpClient(
        context: Context,
        requestInterceptor: RequestInterceptor
    ): OkHttpClient {
        val cache = Cache(context.cacheDir, 10 * 1024 * 1024)

        val builder = OkHttpClient.Builder()
            .addInterceptor(requestInterceptor)
            .readTimeout(120, TimeUnit.SECONDS)
            .connectTimeout((120).toLong(), TimeUnit.SECONDS)
            .writeTimeout(120, TimeUnit.SECONDS)
            .cache(cache)

        return builder.build()
    }

    @Provides
    @Singleton
    fun provideRestAdapter(okHttpClient: OkHttpClient): Retrofit {
        val builder = Retrofit.Builder()
        builder.client(okHttpClient)
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
        return builder.build()
    }

    @Provides
    @Singleton
    fun provideApiService(restAdapter: Retrofit): ApiService {
        return restAdapter.create<ApiService>(ApiService::class.java)
    }

}