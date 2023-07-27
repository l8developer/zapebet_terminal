package com.bet.mpos.api.pojo;

import androidx.annotation.NonNull;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class Fees {

    @SerializedName("success")
    public String success;

    @SerializedName("pix")
    public Data pix;

    @SerializedName("debit")
    public Data debit;

    @SerializedName("credit")
    public ArrayList<DataInstallment> credit;

    @SerializedName("buyer_mdr")
    public ArrayList<DataInstallment> buyer_mdr;

    @SerializedName("debit_buyer_mdr")
    public ArrayList<DataInstallment> debit_buyer_mdr;

    @SerializedName("pix_buyer_mdr")
    public ArrayList<DataInstallment> pix_buyer_mdr;

    @SerializedName("debit_buyer_mdr_pixxou")
    public ArrayList<DataInstallment> debit_buyer_mdr_pixxou;

    @SerializedName("pix_buyer_mdr_pixxou")
    public ArrayList<DataInstallment> pix_buyer_mdr_pixxou;

    @SerializedName("buyer_mdr_pixxou")
    public ArrayList<DataInstallment> buyer_mdr_pixxou;

    public class Data{

        @SerializedName("installments")
        public Integer installments;
        @SerializedName("total_value")
        public Integer total_value;

        @NonNull
        @Override
        public String toString() {
            return "{" +
                    "\ninstallments: "+installments+
                    "\ntotal_value: "+total_value+
                    "\n}";
        }
    }

    public class DataInstallment{

        @SerializedName("installments")
        public Integer installments;
        @SerializedName("total_value")
        public Integer total_value;
        @SerializedName("installment_value")
        public Integer installment_value;

        @NonNull
        @Override
        public String toString() {
            return "{" +
                    "\ninstallments: "+installments+
                    "\ntotal_value: "+total_value+
                    "\ninstallment_value: "+installment_value
                    +"\n}";
        }
    }

    @NonNull
    @Override
    public String toString() {
        return "{" +
                "\nsuccess: "+success+
                "\npix: "+pix+
                "\ndebit: "+debit+
                "\ncredit: "+credit+
                "\nbuyer_mdr: "+buyer_mdr+
                "\ndebit_buyer_mdr: "+debit_buyer_mdr+
                "\npix_buyer_mdr: "+pix_buyer_mdr+
                "\ndebit_buyer_mdr_pixxou: "+debit_buyer_mdr_pixxou+
                "\npix_buyer_mdr_pixxou: "+pix_buyer_mdr_pixxou+
                "\nbuyer_mdr_pixxou: "+buyer_mdr_pixxou+
                "}";
    }
}
