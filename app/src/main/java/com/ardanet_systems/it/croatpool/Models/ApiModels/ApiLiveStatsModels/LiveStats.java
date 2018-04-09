package com.ardanet_systems.it.croatpool.Models.ApiModels.ApiLiveStatsModels;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class LiveStats {
    @SerializedName("config")
    @Expose
    private Config config;
    public Config getConfig(){
        return config;
    }

    @SerializedName("pool")
    @Expose
    private Pool pool;
    public Pool getPool(){
        return pool;
    }

    @SerializedName("charts")
    @Expose
    private Charts charts;
    public Charts getCharts(){
        return charts;
    }

    @SerializedName("network")
    @Expose
    private Network network;
    public Network getNetwork(){
        return network;
    }
}

