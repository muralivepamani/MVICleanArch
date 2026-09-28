package com.appwise.mvicleanarch.di

import com.appwise.mvicleanarch.data.remote.ProductApi
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {
    private const val BASE_URL = "https://meijer-maui-test-default-rtdb.firebaseio.com/"

    @Provides
    @Singleton
    fun provideOkHttpClient(): OkHttpClient =
        OkHttpClient.Builder()
            // Some CDNs (e.g. meijer.com images) reject OkHttp's default "okhttp/x" User-Agent
            .addInterceptor { chain ->
                val userAgent = System.getProperty("http.agent") ?: "Dalvik/2.1.0"
                chain.proceed(chain.request().newBuilder().header("User-Agent", userAgent).build())
            }
            .addInterceptor(HttpLoggingInterceptor().apply {
                level =
                    HttpLoggingInterceptor.Level.BODY
            })
            .connectTimeout(30, TimeUnit.SECONDS)
            .readTimeout(30, TimeUnit.SECONDS)
            .build()

    @Provides
    @Singleton
    fun provideRetrofit(client: OkHttpClient): Retrofit =
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    @Provides
    @Singleton
    fun provideProductApi(retrofit: Retrofit): ProductApi =
        retrofit.create(ProductApi::class.java)
}

