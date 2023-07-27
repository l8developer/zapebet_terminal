package com.bet.mpos.api.pojo;

import androidx.annotation.NonNull;

import com.google.gson.annotations.SerializedName;

public class APIResponse {

    @SerializedName("ct")
    public String ct;
    @SerializedName("iv")
    public String iv;

    @NonNull
    @Override
    public String toString() {
        return "{"+
                "\"ct\""+":\""+ct+"\""+
                "\n\"iv\""+":\""+iv+"\""+
                "}";
    }
}
