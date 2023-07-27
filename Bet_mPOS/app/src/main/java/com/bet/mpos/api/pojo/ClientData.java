package com.bet.mpos.api.pojo;

import androidx.annotation.NonNull;

import com.google.gson.annotations.SerializedName;

public class ClientData {

    @SerializedName("message")
    public String message;
    @SerializedName("success")
    public Boolean success;
    @SerializedName("document_type")
    public String documentType;
    @SerializedName("document_value")
    public String documentValue;
    @SerializedName("register_name")
    public String registerName;
    @SerializedName("social_name")
    public String socialName;
    @SerializedName("phone_country_code")
    public String phoneCountryCode;
    @SerializedName("phone_number")
    public Long phoneNumber;
    @SerializedName("address_zipcode")
    public Integer addressZipCode;
    @SerializedName("address_line")
    public String addressLine;
    @SerializedName("address_number")
    public String addressNumber;
    @SerializedName("address_neighborhood")
    public String addressNeighborhood;
    @SerializedName("address_country")
    public String addressCountry;
    @SerializedName("address_state")
    public String addressState;
    @SerializedName("address_city")
    public String addressCity;
    @SerializedName("address_complement")
    public String addressComplement;
    @SerializedName("email")
    public String email;
    @SerializedName("pixxou")
    public Integer pixxou;
    @SerializedName("customer_configs")
    public Integer customerConfigs;
    @SerializedName("financial")
    public Integer financial;

    @SerializedName("pos_terminal_exist")
    public Boolean posTerminalExist;

    @NonNull
    @Override
    public String toString() {
        return  "success:"+success +";"+
                "\ndocument_type:"+documentType+";"+                  // 0
                "\ndocument_value:"+documentValue+";"+                // 1
                "\nregister_name:"+registerName+";"+                  // 2
                "\nsocial_name:"+socialName+";"+                      // 3
                "\nphone_country_code:"+phoneCountryCode+";"+         // 4
                "\nphone_number:"+phoneNumber+";"+                    // 5
                "\naddress_zipcode:"+addressZipCode+";"+              // 6
                "\naddress_line:"+addressLine+";"+                    // 7
                "\naddress_number:"+addressNumber+";"+                // 8
                "\naddress_neighborhood:"+addressNeighborhood+";"+    // 9
                "\naddress_country:"+addressCountry+";"+              // 10
                "\naddress_state:"+addressState+";"+                  // 11
                "\naddress_city:"+addressCity+";"+                    // 12
                "\naddress_complement:"+addressComplement+";"+        // 13
                "\nemail:"+email+";"+
                "\npixxou: "+pixxou+";"+
                "\ncustomer_configs: "+customerConfigs+";"+
                "\nfinancial: "+financial+";"+
                "\npos_terminal_exists: "+posTerminalExist+";";

    }

}
