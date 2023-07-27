package com.bet.mpos.api.pojo;

import androidx.annotation.NonNull;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class ReportTransactions {

    @SerializedName("message")
    public String message;
    @SerializedName("result")
    public Type result;
    @SerializedName("success")
    public String success;

    public class Type{
        @SerializedName("debit")
        public ArrayList<Data> debit;
        @SerializedName("installments")
        public ArrayList<Data> installments;
        @SerializedName("credit")
        public ArrayList<Data> credit;
        @SerializedName("pix")
        public ArrayList<Data> pix;

        @NonNull
        @Override
        public String toString() {
            return "\n\t\t{" +
                        "\n\t\t\t'debit': [" +
                                        "\n\t\t\t\t" + debit +
                                    "\n\t\t\t]," +
                        "\n\t\t\t'installments': [" +
                                        "\n\t\t\t\t" + installments +
                                    "\n\t\t\t]," +
                        "\n\t\t\t'credit': [" +
                                        "\n\t\t\t\t" + credit +
                                    "\n\t\t\t]," +
                        "\n\t\t\t'pix': [" +
                                        "\n\t\t\t\t" + pix +
                                    "\n\t\t\t]," +
                    "\n\t\t}";
        }
    }

    public class Data{
        @SerializedName("type")
        public Integer type;
        @SerializedName("sales")
        public Integer sales;
        @SerializedName("gross")
        public String gross;
        @SerializedName("net")
        public String net;

        @NonNull
        @Override
        public String toString() {
            return "\n\t\t\t\t{" +
                    "\n\t\t\t\t\t'type': " + type + "," +
                    "\n\t\t\t\t\t'sales': " + sales + "," +
                    "\n\t\t\t\t\t'gross': " + gross + "," +
                    "\n\t\t\t\t\t'net': " + net +
                    "\n\t\t\t\t},";
        }
    }

    @NonNull
    @Override
    public String toString() {
        return "{" +
                "\n\t'message':" + message + "," +
                "\n\t'result':" + result + "," +
                "\n\t'success':" + success +
                "}";
    }
}
