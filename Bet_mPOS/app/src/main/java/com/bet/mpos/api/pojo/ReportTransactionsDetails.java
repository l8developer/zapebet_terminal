package com.bet.mpos.api.pojo;

import androidx.annotation.NonNull;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class ReportTransactionsDetails {

    @SerializedName("message")
    public String message;
    @SerializedName("result")
    public Transactions result;
    @SerializedName("success")
    public String success;

    public class Transactions{
        @SerializedName("transactions")
        public ArrayList<ArrayList<Transaction>> data;

        @NonNull
        @Override
        public String toString() {
            return "\n{\n" +
                    "data: " + data +
                    "\n}";
        }
    }
    public class Transaction {

        @SerializedName("transaction_id")
        public String transaction_id;
        @SerializedName("entry_date")
        public String entry_date;
        @SerializedName("compare")
        public String compare;
        @SerializedName("transaction_type")
        public Integer transaction_type;
        @SerializedName("status")
        public Integer status;
        @SerializedName("value_gross")
        public Integer value_gross;
        @SerializedName("value_net")
        public Integer value_net;
        @SerializedName("card_flag")
        public String card_flag;
        @SerializedName("total_installments")
        public Integer total_installments;
        @SerializedName("card_last_dig")
        public String card_last_dig;

//        @NonNull
//        @Override
//        public String toString() {
//            return "\n{" +
//                    "\n transaction_id: " + transaction_id +
//                    "\n entry_date: " + entry_date +
//                    "\n compare: " + compare+
//                    "\n transaction_type: " + transaction_type+
//                    "\n status: " + status +
//                    "\n value_gross: " + value_gross+
//                    "\n value_net: " + value_net+
//                    "\n card_flag: " + card_flag+
//                    "\n total_installments: " + total_installments+
//                    "\n card_last_dig: " + card_last_dig+
//                    "\n}";
//        }

        @NonNull
        @Override
        public String toString() {
            return "\n{" +
                    "\n \"transaction_id\": " + "\""+transaction_id +"\","+
                    "\n \"entry_date\": " + "\""+entry_date +"\","+
                    "\n \"compare\": " + "\""+compare+"\","+
                    "\n \"transaction_type\": " + transaction_type+","+
                    "\n \"status\": " + status +","+
                    "\n \"value_gross\": " + value_gross+","+
                    "\n \"value_net\": " + value_net+","+
                    "\n \"card_flag\": " + "\""+card_flag+"\","+
                    "\n \"total_installments\": " + total_installments+","+
                    "\n \"card_last_dig\": " + card_last_dig+","+
                    "\n}";
        }
    }

    @NonNull
    @Override
    public String toString() {
        return "message: "+ message+
                "\nresult: "+ result+
                "\nsuccess: "+ success;
    }
}
