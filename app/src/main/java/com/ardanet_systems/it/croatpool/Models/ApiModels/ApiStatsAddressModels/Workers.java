package com.ardanet_systems.it.croatpool.Models.ApiModels.ApiStatsAddressModels;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Workers {
    @SerializedName("name")
    @Expose
    private String name;

    @SerializedName("hashrate")
    @Expose
    private String hashrate;

    @SerializedName("lastShare")
    @Expose
    private String lastShare;

    @SerializedName("hashes")
    @Expose
    private String hashes;

    public String getName() { return this.name; }
    public String getHashrate() { return this.hashrate; }
    public String getLastShare() { return this.lastShare; }
    public String getHashes() { return this.hashes; }
}
