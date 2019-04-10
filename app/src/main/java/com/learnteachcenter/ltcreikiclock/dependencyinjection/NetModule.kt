package com.learnteachcenter.ltcreikiclock.dependencyinjection

import android.content.Context
import android.se.omapi.Session
import com.learnteachcenter.ltcreikiclock.Utils
import com.learnteachcenter.ltcreikiclock.auth.SessionManager
import com.learnteachcenter.ltcreikiclock.data.Position
import com.learnteachcenter.ltcreikiclock.data.Reiki
import com.learnteachcenter.ltcreikiclock.data.source.remote.ApiInterface
import com.squareup.moshi.*
import dagger.Module
import dagger.Provides
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.Retrofit.Builder
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.moshi.MoshiConverterFactory
import java.lang.reflect.Type
import javax.inject.Singleton

// https://futurestud.io/tutorials/retrofit-2-manage-request-headers-in-okhttp-interceptor

@Module
class NetModule (private val baseUrl: String, private val context: Context) {

    @Provides
    @Singleton
    fun providesOkHttpClient(): OkHttpClient {

        val session = SessionManager(context)
        val token = session.getToken()

        val httpClient = OkHttpClient.Builder()

        httpClient.addInterceptor { chain ->
            val original = chain.request()

            // Request customization: add request headers
            val requestBuilder = original.newBuilder()
                    .header("Authorization", "Bearer " + token) // <-- this is the important line

            val request = requestBuilder.build()
            chain.proceed(request)
        }

        val client = httpClient.build()

        return client
    }

    @Provides
    @Singleton
    fun providesMoshi(): Moshi {
        return Moshi.Builder()
                .add(KotlinJsonAdapterFactory())
                .build()
    }

    @Provides
    @Singleton
    fun providesRetrofit(okHttpClient: OkHttpClient, moshi: Moshi): Retrofit {
        return Builder().client(okHttpClient).baseUrl(baseUrl)
                .addConverterFactory(MoshiConverterFactory.create(moshi))
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build()
    }

    @Provides
    @Singleton
    fun providesApiInterface(retrofit: Retrofit): ApiInterface = retrofit.create(
            ApiInterface::class.java
    )
}