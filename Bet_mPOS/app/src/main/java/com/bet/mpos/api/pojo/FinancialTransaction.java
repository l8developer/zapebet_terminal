package com.bet.mpos.api.pojo;

import androidx.annotation.NonNull;

import com.google.gson.annotations.SerializedName;

public class FinancialTransaction {

    public FinancialTransaction(String agent_financial_hash, int loan_value, int r_value, String document_value, String card_front_img, String card_back_img, String doc_front_img, String doc_back_img, String selfie_img, String selfie_card_img, String serial_number) {
        this.agent_financial_hash = agent_financial_hash;
        this.loan_value = loan_value;
        this.r_value = r_value;
        this.document_value = document_value;
        this.card_front_img = card_front_img;
        this.card_back_img = card_back_img;
        this.doc_front_img = doc_front_img;
        this.doc_back_img = doc_back_img;
        this.selfie_img = selfie_img;
        this.selfie_card_img = selfie_card_img;
        this.serial_number = serial_number;
    }

    @SerializedName("agent_financial_hash")
    public String agent_financial_hash;

    @SerializedName("loan_value")
    public int loan_value;

    @SerializedName("r_value")
    public int r_value;

    @SerializedName("document_value")
    public String document_value;

    @SerializedName("card_front_img")
    public String card_front_img;

    @SerializedName("card_back_img")
    public String card_back_img;

    @SerializedName("doc_front_img")
    public String doc_front_img;

    @SerializedName("doc_back_img")
    public String doc_back_img;

    @SerializedName("selfie_img")
    public String selfie_img;

    @SerializedName("selfie_card_img")
    public String selfie_card_img;

    @SerializedName("serial_number")
    public String serial_number;

    @SerializedName("pix_key")
    public String pix_key;

    @NonNull
    @Override
    public String toString() {
        return "{" +
                "\n\"agent_financial_hash\": \""+agent_financial_hash+"\","+
                "\n\"loan_value\": "+loan_value+","+
                "\n\"r_value\": "+r_value+","+
                "\n\"document_value\": \""+document_value+"\","+
                "\n\"card_front_img\": \""+card_front_img+"\","+
                "\n\"card_back_img\": \""+card_back_img+"\","+
                "\n\"doc_front_img\": \""+doc_front_img+"\","+
                "\n\"doc_back_img\": \""+doc_back_img+"\","+
                "\n\"selfie_img\": \""+selfie_img+"\","+
                "\n\"selfie_card_img\": \""+selfie_card_img+"\","+
                "\n\"serial_number\": \""+serial_number+"\","+
                "\n\"pix_key\": \""+pix_key+"\""+
                "\n}";
    }
}
