package com.ardanet_systems.it.croatpool.Models.ApiModels.ApiLiveStatsModels;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Network {
    @SerializedName("difficulty")
    @Expose
    private int difficulty;

    public Network(int difficulty){
        this.difficulty = difficulty;
    }

    public int getDifficulty() {
        return difficulty;
    }
}
