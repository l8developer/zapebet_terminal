package com.bet.update.api;

import androidx.annotation.NonNull;

import com.google.gson.annotations.SerializedName;

public class UpdateResponse {
    @SerializedName("version")
    public Integer version;
    @SerializedName("version_name")
    public String version_name;

    @SerializedName("link_download")
    public String link_download;

    @NonNull
    @Override
    public String toString() {
        return  "version:"+version +";"+
                "\nlink_download:"+link_download+";"+
                "\nversion_name:"+version_name+";";

    }
}
