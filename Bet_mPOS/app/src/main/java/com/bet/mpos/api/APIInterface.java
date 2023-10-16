package com.bet.mpos.api;

import com.bet.mpos.api.pojo.APIResponse;
import com.bet.mpos.api.pojo.GameOddsResponse;
import com.bet.mpos.objects.BetCustomerRegistration;
import com.bet.mpos.objects.BetGamesFromLeague;
import com.bet.mpos.objects.BetLeaguesFromCountry;
import com.bet.mpos.objects.BetRegistration;
import com.google.api.client.json.Json;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface APIInterface {

//    @FormUrlEncoded
//    @POST("terminal/customerdata")
//    Call<ClientData> getCustomerData(@Field("serial_number") String serialNumber);

    @FormUrlEncoded
    @POST("terminal/customerdata")
    Call<APIResponse> getCustomerData(@Field("ct") String ct,
                                      @Field("iv") String iv);

//    @FormUrlEncoded
//    @POST("terminal/simulation")
//    Call<Fees> getCustomerFee(@Field("amount_gross") Integer amount_gross);

    @FormUrlEncoded
    @POST("terminal/simulation")
    Call<APIResponse> getCustomerFee(@Field("ct") String ct,
                                     @Field("iv") String iv);

//    @FormUrlEncoded
//    @POST("terminal/pos_terminal_transaction_details")
//    Call<ReportTransactionsDetails> getTerminalTransactionsDetails(@Field("serial_number") String serial_number,
//                                                                   @Field("start_date") String start_date,
//                                                                   @Field("end_date") String end_date,
//                                                                   @Field("type") String type
//    );

    @FormUrlEncoded
    @POST("terminal/pos_terminal_transaction_details")
    Call<APIResponse> getTerminalTransactionsDetails(@Field("ct") String ct,
                                                                   @Field("iv") String iv
    );

//    @FormUrlEncoded
//    @POST("terminal/pos_terminal_transaction")
//    Call<ReportTransactions> getTerminalTransactions(@Field("serial_number") String serial_number,
//                                                     @Field("start_date") String start_date,
//                                                     @Field("end_date") String end_date
//    );

    @FormUrlEncoded
    @POST("terminal/pos_terminal_transaction")
    Call<APIResponse> getTerminalTransactions(  @Field("ct") String ct,
                                                @Field("iv") String iv
    );

    @FormUrlEncoded
    @POST("terminal/pos_terminal_version")
    Call<APIResponse> getVersion(  @Field("ct") String ct,
                                                @Field("iv") String iv
    );

//    @FormUrlEncoded
//    @POST("terminal/pos_terminal_financial")
//    Call<Financial> getTerminalFinancial(@Field("amount_gross") int amount_gross,
//                                         @Field("serial_number") String serial_number,
//                                         @Field("r") int r
//    );

    @FormUrlEncoded
    @POST("terminal/pos_terminal_financial")
    Call<APIResponse> getTerminalFinancial(@Field("ct") String ct,
                                         @Field("iv") String iv
    );

    @FormUrlEncoded
    @POST("terminal/financial_transaction")
    Call<APIResponse> financial_transaction(@Field("ct") String ct,
                                           @Field("iv") String iv
    );

//    @FormUrlEncoded
//    @POST("terminal/financial_transaction")
//    Call<APIResponse> financial_transaction(@Field("agent_financial_hash") String agent_financial_hash,
//                                            @Field("loan_value") int loan_value,
//                                            @Field("r_value") int r_value,
//                                            @Field("document_value") String document_value,
//                                            @Field("card_front_img") String card_front_img,
//                                            @Field("card_back_img") String card_back_img,
//                                            @Field("doc_front_img") String doc_front_img,
//                                            @Field("doc_back_img") String doc_back_img,
//                                            @Field("selfie_img") String selfie_img,
//                                            @Field("selfie_card_img") String selfie_card_img,
//                                            @Field("serial_number") String serial_number
//    );

    //BET
    @FormUrlEncoded
    @POST("bot/customer_registration_terminal")
    Call<BetCustomerRegistration> customer_registration(@Header("zb-token") String zbToken,
                                                        @Field("document_value") String document,
                                                        @Field("phone_number") String phone
    );

//    @FormUrlEncoded
//    @POST("bot/customer_registration_terminal")
//    Call<Json> customer_registration(@Header("zb-token") String zbToken,
//                                     @Field("document") String document,
//                                     @Field("phone_number") String phone
//    );

    @FormUrlEncoded
    @POST("bot/register_bet_terminal")
    Call<Json> register_bet(@Header("zb-token") String zbToken,
                            @Field("option") String option,
                            @Field("customer") String customer,
                            @Field("value") int value
    );

    @FormUrlEncoded
    @POST("bot/store_bet_terminal")
    Call<Json> store_bet(@Header("zb-token") String zbToken,
                            @Field("id") String id, // customer id
                            @Field("bet_odd") int bet_odd, // odd id
                            @Field("value_paid") int value_paid // value paid
    );

    @GET("bot/game_and_odds")
    Call<ArrayList<GameOddsResponse>> getGameAndOdds(@Header("zb-token") String zbToken
    );

    // SERVIÇOS


    // NEW ZAPEBET

    @GET("v1/site/leagues/{country}")
    Call<BetLeaguesFromCountry>getLeaguesFromCountry(@Path("country") String country);

    @GET("v1/site/games/{league_id}")
    Call<BetGamesFromLeague>getGamesFromLeague(@Path("league_id") int league_id);


}
