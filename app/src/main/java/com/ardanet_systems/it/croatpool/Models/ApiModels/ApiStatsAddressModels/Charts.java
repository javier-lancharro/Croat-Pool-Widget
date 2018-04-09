package com.ardanet_systems.it.croatpool.Models.ApiModels.ApiStatsAddressModels;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Charts {
    @SerializedName("payments")
    @Expose
    private List<List<Long>> payments = null;

    @SerializedName("hashrate")
    @Expose
    private List<List<Long>> hashrate = null;

    public List<List<Long>> getPayments() {
        return payments;
    }
    public List<List<Long>> getHashrate() {
        return hashrate;
    }
}
