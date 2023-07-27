package com.bet.mpos.objects;

public class EncryptedSerialNumber {

    private String serialNumber;

    public EncryptedSerialNumber(String serialNumber) {
        this.serialNumber = serialNumber;
    }

    public String getSerialNumber() {
        return serialNumber;
    }

    public void setSerialNumber(String serialNumber) {
        this.serialNumber = serialNumber;
    }

    @Override
    public String toString() {
        return "{"
                +"\"serial_number\""+":\""+serialNumber+"\""+
                "}";
    }
}
