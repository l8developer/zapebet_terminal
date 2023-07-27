package com.bet.mpos.api.pojo;

import androidx.annotation.NonNull;

import com.google.gson.annotations.SerializedName;

public class UpdateResponse {

    @SerializedName("version")
    public Integer version;
    @SerializedName("version_name")
    public String version_name;

    @NonNull
    @Override
    public String toString() {
        return  "version:"+version +";"+
                "\nversion_name:"+version_name+";";

    }
}
