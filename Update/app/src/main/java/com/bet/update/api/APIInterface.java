package com.bet.update.api;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

public interface APIInterface {


    @FormUrlEncoded
    @POST("terminal/pos_terminal_version")
    Call<APIResponse> getVersion(  @Field("ct") String ct,
                                   @Field("iv") String iv
    );

}
