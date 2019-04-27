package com.learnteachcenter.ltcreikiclock.di

import com.google.firebase.auth.FirebaseAuth
import com.learnteachcenter.ltcreikiclock.data.source.remote.ApiInterface
import com.learnteachcenter.ltcreikiclock.authentication.FirebaseAuthenticationInterface
import com.learnteachcenter.ltcreikiclock.authentication.FirebaseAuthenticationManager
import com.squareup.moshi.*
import dagger.Module
import dagger.Provides
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.Retrofit.Builder
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.moshi.MoshiConverterFactory
import javax.inject.Singleton

// https://futurestud.io/tutorials/retrofit-2-manage-request-headers-in-okhttp-interceptor

@Module
class NetModule (private val baseUrl: String) {

    @Provides
    @Singleton
    fun providesFirebaseAuth(): FirebaseAuth = FirebaseAuth.getInstance()

    @Provides
    fun providesAuthentication(firebaseAuth: FirebaseAuth): FirebaseAuthenticationInterface {
        return FirebaseAuthenticationManager(firebaseAuth)
    }

    @Provides
    @Singleton
    fun providesOkHttpClient(authentication: FirebaseAuthenticationInterface): OkHttpClient {

        val token = authentication.getIdToken()

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