package com.bet.mpos.util;

import android.content.Context;
import android.graphics.Bitmap;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Base64;
import android.util.Log;
import android.util.Pair;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.common.BitMatrix;
import com.journeyapps.barcodescanner.BarcodeEncoder;
import com.bet.mpos.BuildConfig;
import com.bet.mpos.BetApp;
import com.bet.mpos.R;

import org.apache.commons.codec.binary.Hex;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.SecureRandom;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

public class Functions {

    public static double int_to_double(int value){
        double res = (double) value/100;
        return res;
    }
    public static String int_to_real(int i){
        return BetApp.getAppContext().getString(R.string.total_amount, Double.valueOf((double)i/100));
    }

    public static int real_to_int(String s) {
        return Integer.valueOf(s.replace(",","").replace("R$ ", "").replace(".",""));
    }

    public static int real_to_int2(String s) {
        return Integer.valueOf(s.replace(",","").replace("R$", "").replace(".",""));
    }

    public static boolean isConnected()
    {
        ConnectivityManager cm =
                (ConnectivityManager) BetApp.getAppContext().getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        if(!(activeNetwork != null && activeNetwork.isConnectedOrConnecting()))
            return false;
        else
            return true;
    }

    public static Bitmap generateQRCode(String text){
        Bitmap bitmap = null;
        MultiFormatWriter multiFormatWriter = new MultiFormatWriter();
        try{
            BitMatrix bitMatrix = multiFormatWriter.encode(text, BarcodeFormat.QR_CODE, 450, 450);
            BarcodeEncoder barcodeEncoder = new BarcodeEncoder();
            bitmap = barcodeEncoder.createBitmap(bitMatrix);
        }catch (Exception e){
            System.out.println(e);
        }
        return bitmap;
    }

    public static String decrypt(String ct, String iv) {
        try {
//            byte[] key = (byte[]) new Hex().decode("-JaNdRgUjXn2r5u8x/A?D(G+KbPeShVm");
//            SecretKeySpec skeySpec = new SecretKeySpec(key, "AES");
//            SecretKeySpec skeySpec = generateKey("-JaNdRgUjXn2r5u8x/A?D(G+KbPeShVm");
//            IvParameterSpec ivSpec = generateIV("bcffafe6588d3ca63c0aedb5b0090a5d");

            SecretKeySpec skeySpec = generateKey(BuildConfig.KEY_IN);
            IvParameterSpec ivSpec = generateIV(iv);

            Cipher ecipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
            ecipher.init(Cipher.DECRYPT_MODE, skeySpec, ivSpec);

            byte[] raw = Base64.decode(ct, Base64.DEFAULT);

            byte[] originalBytes = ecipher.doFinal(raw);

            String original = new String(originalBytes, "UTF8");

            return original;
        }catch (Exception e){
            Log.e("decrypt: ", e.toString());
            return "";
        }
    }

    public static String decryptDebug(String ct, String iv) {
        try {
//            byte[] key = (byte[]) new Hex().decode("-JaNdRgUjXn2r5u8x/A?D(G+KbPeShVm");
//            SecretKeySpec skeySpec = new SecretKeySpec(key, "AES");
//            SecretKeySpec skeySpec = generateKey("-JaNdRgUjXn2r5u8x/A?D(G+KbPeShVm");
//            IvParameterSpec ivSpec = generateIV("bcffafe6588d3ca63c0aedb5b0090a5d");

            SecretKeySpec skeySpec = generateKey(BuildConfig.KEY_OUT);
            IvParameterSpec ivSpec = generateIV(iv);

            Cipher ecipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
            ecipher.init(Cipher.DECRYPT_MODE, skeySpec, ivSpec);

            byte[] raw = Base64.decode(ct, Base64.DEFAULT);

            byte[] originalBytes = ecipher.doFinal(raw);

            String original = new String(originalBytes, "UTF8");

            return original;
        }catch (Exception e){
            Log.e("decrypt: ", e.toString());
            return "";
        }
    }

    public static Pair<String, String> encrypt(String input){
        try {
            IvParameterSpec ivspec = Functions.generateIV();
            SecretKeySpec secretKey = generateKey(BuildConfig.KEY_OUT);
            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
            cipher.init(Cipher.ENCRYPT_MODE, secretKey, ivspec);
            byte[] cipherText = cipher.doFinal(input.getBytes());
            return Pair.create(Base64.encodeToString(cipherText, Base64.DEFAULT), Functions.hex(ivspec.getIV()));
        } catch (Exception e) {
            Log.e("Error while encrypting: ", e.toString());
            return Pair.create("error", "error");
        }
    }

    private static SecretKeySpec generateKey(String secret) throws Exception {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] encodedhash = digest.digest(
                    secret.getBytes(StandardCharsets.UTF_8));
            SecretKeySpec key = new SecretKeySpec(encodedhash, "AES");
            return key;
        }catch (Exception e) {
            System.out.println(e);
            return null;
        }
    }

    public static IvParameterSpec generateIV(String secret) throws Exception {
        try {
            byte[] ivData = (byte[]) new Hex().decode(secret);
//            byte[] decoded = Base64.decode(secret.getBytes(), Base64.DEFAULT);
//            System.out.println(new String(decoded, StandardCharsets.UTF_8));
            IvParameterSpec iv = new IvParameterSpec( ivData);
//                IvParameterSpec iv = new IvParameterSpec(hexStringToByteArray(secret));
//            IvParameterSpec iv = Util.generateIv(secret);
//            byte[] decoded = Base64.decode(secret.getBytes(), Base64.DEFAULT);
            return iv;
        }catch (Exception e) {
            System.out.println(e);
            return null;
        }
    }

    public static IvParameterSpec generateIV() {
        byte[] iv = new byte[16];
        new SecureRandom().nextBytes(iv);
        return new IvParameterSpec(iv);
    }

    public static String hex(byte[] bytes) {
        StringBuilder result = new StringBuilder();
        for (byte aByte : bytes) {
            result.append(String.format("%02x", aByte));
            // upper case
            // result.append(String.format("%02X", aByte));
        }
        return result.toString();
    }
}
