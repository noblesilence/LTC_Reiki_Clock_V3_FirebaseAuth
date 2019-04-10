package com.learnteachcenter.ltcreikiclock.dependencyinjection

import com.learnteachcenter.ltcreikiclock.Utils
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
class NetModule (private val baseUrl: String) {

    @Provides
    @Singleton
    fun providesOkHttpClient(): OkHttpClient {

        val httpClient = OkHttpClient.Builder()
        httpClient.addInterceptor { chain ->
            val original = chain.request()

            // Request customization: add request headers
            // TODO: add token variable
            val requestBuilder = original.newBuilder()
                    .header("Authorization", "Bearer eyJhbGciOiJSUzI1NiIsImtpZCI6IjdkMmY5ZjNmYjgzZDYzMzc0OTdiNmY3Y2QyY2ZmNGRmYTVjMmU4YjgiLCJ0eXAiOiJKV1QifQ.eyJuYW1lIjoiQXllIEF5ZSBNb24iLCJwaWN0dXJlIjoiaHR0cHM6Ly9saDYuZ29vZ2xldXNlcmNvbnRlbnQuY29tLy1kTDZVMGRkSjB1VS9BQUFBQUFBQUFBSS9BQUFBQUFBQUNBMC9qajB2N09yQmJ2RS9zOTYtYy9waG90by5qcGciLCJpc3MiOiJodHRwczovL3NlY3VyZXRva2VuLmdvb2dsZS5jb20vbHRjLXJlaWtpLWNsb2NrIiwiYXVkIjoibHRjLXJlaWtpLWNsb2NrIiwiYXV0aF90aW1lIjoxNTU0ODk2ODUyLCJ1c2VyX2lkIjoieUhOUzlkcDhKeGNNaVQ0Z3RJR0FEZU1mcGRrMSIsInN1YiI6InlITlM5ZHA4SnhjTWlUNGd0SUdBRGVNZnBkazEiLCJpYXQiOjE1NTQ4OTY4NTQsImV4cCI6MTU1NDkwMDQ1NCwiZW1haWwiOiJub2JsZXNpbGVuY2VAZ21haWwuY29tIiwiZW1haWxfdmVyaWZpZWQiOnRydWUsImZpcmViYXNlIjp7ImlkZW50aXRpZXMiOnsiZ29vZ2xlLmNvbSI6WyIxMTAyNzgxNTQ1MTY2NTkzODQwMDEiXSwiZW1haWwiOlsibm9ibGVzaWxlbmNlQGdtYWlsLmNvbSJdfSwic2lnbl9pbl9wcm92aWRlciI6Imdvb2dsZS5jb20ifX0.PNwkKUDDRdEdqZ3YIBfxeCVaRkp0gnRdX3AUm9bB1bK9mpR-R4I9HyJM5AwZSwWGJjqkWrdWpucdQNsLweX93WFzptFl5OhauOfwEEQ-9bn7M5lhLcSFc8DKiFR7RZwOKy9kHT4Lo3JzyaoKzcroAvEO1UlF-35Lde-LJr8x5Pr0jpQiaLnQC9o8HCGUeohMk4SrGpOOUP-k63r5UyVusMNtH_e_Ryc5SqnizPHbw9pTJT4v5jTa0gWyqVwdhvSXrtEomrhQ07rpVcFsMozeFUIIwi3rjKWxr9Ir8x1g-5EtXXxd8R7csAHn0zgC6kC3FjEH7GjXFIVBjhgatULJfA") // <-- this is the important line

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