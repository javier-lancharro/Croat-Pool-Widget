package com.ardanet_systems.it.croatpool.Models.ApiModels.ApiStatsAddressModels;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class StatsAddress {
    @SerializedName("hashes")
    @Expose
    private String hashes;

    @SerializedName("lastShare")
    @Expose
    private String lastShare;

    @SerializedName("balance")
    @Expose
    private String balance;

    @SerializedName("paid")
    @Expose
    private String paid;
    @SerializedName("hashrate")
    @Expose
    private String hashrate;

    public String getHashes() {
        return hashes;
    }
    public String getLastShare() {
        return lastShare;
    }
    public String getBalance() {
        return balance;
    }
    public String getPaid() {
        return paid;
    }
    public String getHashrate() {
        return hashrate;
    }
}
