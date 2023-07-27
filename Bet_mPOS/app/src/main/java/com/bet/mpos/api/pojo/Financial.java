package com.bet.mpos.api.pojo;

import androidx.annotation.NonNull;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class Financial {

    @SerializedName("message")
    public String message;

    @SerializedName("result")
    public Result result;
    public class Result {
        @SerializedName("financeira")
        public ArrayList<Financeira> financeira;

        @NonNull
        @Override
        public String toString() {
            return "\nfinanceira:" +
                    "[\n" +
                        financeira +
                    "]";
        }
    }

    public class Financeira {
        @SerializedName("installments")
        public Integer installments;

        @SerializedName("amount_gross")
        public Integer amountGross;

        @SerializedName("amount_installments")
        public Integer amountInstallments;

        @SerializedName("mdr_value")
        public Integer mdrValue;

        @SerializedName("r_value")
        public Integer rValue;

        @SerializedName("amount_final")
        public Integer amountFinal;

        @NonNull
        @Override
        public String toString() {
            return  "{" +
                    "\n\tinstallments:" + installments + "," +
                    "\n\tamount_gross:" + amountGross + "," +
                    "\n\tamount_installments:" + amountInstallments + "," +
                    "\n\tmdr_value:" + mdrValue + "," +
                    "\n\tr_value:" + rValue + "," +
                    "\n\tamount_final:" + amountFinal + "," +
                    "\n},\n";
        }
    }

    @NonNull
    @Override
    public String toString() {
        return "{" +
                "\nmessage:" + message + "," +
                "\nresult:" + result +
                "}";
    }
}
