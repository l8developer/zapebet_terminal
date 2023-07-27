package com.bet.mpos.util;

import android.os.Build;

import com.bet.mpos.BuildConfig;

public class SerialNumber {

    private String SN;
    public SerialNumber() {
        if(BuildConfig.TERMINAL_SERIAL_NUMBER == "")
            SN = Build.SERIAL;
        else
            SN = BuildConfig.TERMINAL_SERIAL_NUMBER;
    }

    public String getSN() {
//        return "1492203238";
        return "1492203195";
//        return "69696969";
//        return Build.SERIAL;
    }
}
