package com.learnteachcenter.ltcreikiclock.data;

import com.learnteachcenter.ltcreikiclock.data.ReikiResponse;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;

public interface ReikiApiService {
    @GET("reikis/sample")
    Call<ReikiResponse> getSampleReiki();
}
