package com.learnteachcenter.ltcreikiclock.data.source.remote

import com.learnteachcenter.ltcreikiclock.data.Reiki
import io.reactivex.Observable
import retrofit2.http.GET

interface ApiInterface {
    @GET("reikis/default")
    fun getSampleReikis(): Observable<List<Reiki>>

    @GET("reikis")
    fun getReikis(): Observable<List<Reiki>>
}