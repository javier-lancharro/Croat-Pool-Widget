package com.ardanet_systems.it.croatpool.Models.ApiModels.ApiStatsAddressModels;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class PoolStats {

    @SerializedName("stats")
    @Expose
    private StatsAddress statsAddress;

    @SerializedName("payments")
    @Expose
    private List<String> payments = null;

    @SerializedName("charts")
    @Expose
    private Charts charts;

    @SerializedName("workers")
    @Expose
    private List<Workers> workers;

    public StatsAddress getStatsAddress() {
        return statsAddress;
    }
    public List<String> getPayments() {
        return payments;
    }
    public Charts getCharts() {
        return charts;
    }
    public List<Workers> getWorkers() { return workers; }
}
