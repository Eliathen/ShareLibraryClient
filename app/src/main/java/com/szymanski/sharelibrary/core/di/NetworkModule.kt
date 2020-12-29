package com.szymanski.sharelibrary.core.di

import com.szymanski.sharelibrary.BuildConfig
import com.szymanski.sharelibrary.core.api.Api
import com.szymanski.sharelibrary.core.network.HeaderInterceptor
import com.szymanski.sharelibrary.core.utils.API_URL
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

val networkModule = module {


    single<Interceptor> {
        HttpLoggingInterceptor()
            .apply {
                level = if (BuildConfig.DEBUG)
                    HttpLoggingInterceptor.Level.BODY
                else
                    HttpLoggingInterceptor.Level.NONE
            }
    }

    single {
        Retrofit.Builder()
            .baseUrl(API_URL)
            .client(get<OkHttpClient>())
            .addConverterFactory(get<GsonConverterFactory>())
            .build()
    }

    single<GsonConverterFactory> {
        GsonConverterFactory.create()
    }

    single {
        OkHttpClient.Builder()
            .addInterceptor(get<Interceptor>())
            .addInterceptor(HeaderInterceptor(get()))
            .build()
    }


    single {
        get<Retrofit>().create(Api::class.java)
    }

}