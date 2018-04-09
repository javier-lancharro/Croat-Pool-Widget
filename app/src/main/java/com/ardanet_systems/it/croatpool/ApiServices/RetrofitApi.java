package com.ardanet_systems.it.croatpool.ApiServices;

import com.ardanet_systems.it.croatpool.Models.ApiModels.ApiLiveStatsModels.LiveStats;
import com.ardanet_systems.it.croatpool.Models.ApiModels.ApiStatsAddressModels.PoolStats;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.Query;

public interface RetrofitApi {
    @Headers({
            "Content-Type: application/json;charset=utf-8",
            "Accept: application/json"
    })
    @GET("live_stats")
    Call<LiveStats> getLiveStats();

    @GET("stats_address")
    Call<PoolStats> getStatsAddress(@Query("address") String address);

    @GET("stats_address")
    Call<PoolStats> getStatsWorker(@Query("address") String address);
}
