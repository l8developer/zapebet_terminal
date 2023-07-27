package com.bet.mpos.api.pojo;

import androidx.annotation.NonNull;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class GameOddsResponse {

    @SerializedName("uuid")
    public String uuid;

    @SerializedName("game")
    public String game;

    @SerializedName("place")
    public String place;

    @SerializedName("initial_game")
    public String initial_game;

    @SerializedName("option")
    public ArrayList<GameOddsResponse.Option> option;

    public class Option {
        @SerializedName("uuid")
        public String uuid;

        @SerializedName("shield")
        public String shield;

        @SerializedName("team")
        public String team;

        @SerializedName("odd")
        public String odd;

        @NonNull
        @Override
        public String toString() {
            return  "{" +
                    "\n\tuuid:" + uuid + "," +
                    "\n\tshield:" + shield + "," +
                    "\n\tteam:" + team + "," +
                    "\n\todd:" + odd + "," +
                    "\n},\n";
        }
    }

    @NonNull
    @Override
    public String toString() {
        return "{" +
                "\nuuid:" + uuid + "," +
                "\ngame:" + game + "," +
                "\nplace:" + place + "," +
                "\ninitial_game:" + initial_game + "," +
                "\noption:" + option +
                "}";
    }
}
