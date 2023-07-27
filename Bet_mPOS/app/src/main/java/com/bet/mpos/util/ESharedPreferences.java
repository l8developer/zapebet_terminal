package com.bet.mpos.util;

import android.content.SharedPreferences;
import android.util.Log;

import androidx.security.crypto.EncryptedSharedPreferences;

import com.bet.mpos.BetApp;

public class ESharedPreferences {

    private static SharedPreferences preferenceFile;

    public static SharedPreferences getInstance(String fileName, String masterKey){
        try {
            preferenceFile = EncryptedSharedPreferences.create(
                fileName,
                masterKey,
                BetApp.getAppContext(),
                EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
                EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
            );
            return preferenceFile;
        }catch(Exception e){
            Log.e("ESP getInstance", e.toString());
            return null;
        }
    }
}
