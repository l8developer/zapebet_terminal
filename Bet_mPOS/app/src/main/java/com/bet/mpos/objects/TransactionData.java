package com.bet.mpos.objects;

import java.io.Serializable;

public class TransactionData implements Serializable {
    private int value, paymentType, installment;
    private String status, brand, address, sellerName, acquiring, pan, autoCode, documentType, document,
    nsu, date, hour, cv, arqc, aid, sellerReceipt, customerReceipt, approvalMessage, aidLabel, transactionId, idPix;

    public TransactionData() {
    }

    public TransactionData(int value, int paymentType, int installment, String status, String brand, String address, String sellerName, String acquiring, String pan, String autoCode, String documentType, String document, String nsu, String date, String hour, String cv, String arqc, String aid, String sellerReceipt, String customerReceipt, String approvalMessage, String aidLabel, String transactionId, String idPix) {
        this.value = value;
        this.paymentType = paymentType;
        this.installment = installment;
        this.status = status;
        this.brand = brand;
        this.address = address;
        this.sellerName = sellerName;
        this.acquiring = acquiring;
        this.pan = pan;
        this.autoCode = autoCode;
        this.documentType = documentType;
        this.document = document;
        this.nsu = nsu;
        this.date = date;
        this.hour = hour;
        this.cv = cv;
        this.arqc = arqc;
        this.aid = aid;
        this.sellerReceipt = sellerReceipt;
        this.customerReceipt = customerReceipt;
        this.approvalMessage = approvalMessage;
        this.aidLabel = aidLabel;
        this.transactionId = transactionId;
        this.idPix = idPix;
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }

    public int getPaymentType() {
        return paymentType;
    }

    public void setPaymentType(int paymentType) {
        this.paymentType = paymentType;
    }

    public int getInstallment() {
        return installment;
    }

    public void setInstallment(int installment) {
        this.installment = installment;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getSellerName() {
        return sellerName;
    }

    public void setSellerName(String sellerName) {
        this.sellerName = sellerName;
    }

    public String getAcquiring() {
        return acquiring;
    }

    public void setAcquiring(String acquiring) {
        this.acquiring = acquiring;
    }

    public String getPan() {
        return pan;
    }

    public void setPan(String pan) {
        this.pan = pan;
    }

    public String getAutoCode() {
        return autoCode;
    }

    public void setAutoCode(String autoCode) {
        this.autoCode = autoCode;
    }

    public String getDocumentType() {
        return documentType;
    }

    public void setDocumentType(String documentType) {
        this.documentType = documentType;
    }

    public String getDocument() {
        return document;
    }

    public void setDocument(String document) {
        this.document = document;
    }

    public String getNsu() {
        return nsu;
    }

    public void setNsu(String nsu) {
        this.nsu = nsu;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getHour() {
        return hour;
    }

    public void setHour(String hour) {
        this.hour = hour;
    }

    public String getCv() {
        return cv;
    }

    public void setCv(String cv) {
        this.cv = cv;
    }

    public String getArqc() {
        return arqc;
    }

    public void setArqc(String arqc) {
        this.arqc = arqc;
    }

    public String getAid() {
        return aid;
    }

    public void setAid(String aid) {
        this.aid = aid;
    }

    public String getSellerReceipt() {
        return sellerReceipt;
    }

    public void setSellerReceipt(String sellerReceipt) {
        this.sellerReceipt = sellerReceipt;
    }

    public String getCustomerReceipt() {
        return customerReceipt;
    }

    public void setCustomerReceipt(String customerReceipt) {
        this.customerReceipt = customerReceipt;
    }

    public String getApprovalMessage() {
        return approvalMessage;
    }

    public void setApprovalMessage(String approvalMessage) {
        this.approvalMessage = approvalMessage;
    }

    public String getAidLabel() {
        return aidLabel;
    }

    public void setAidLabel(String aidLabel) {
        this.aidLabel = aidLabel;
    }

    public String getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(String transactionId) {
        this.transactionId = transactionId;
    }

    public String getIdPix() {
        return idPix;
    }

    public void setIdPix(String idPix) {
        this.idPix = idPix;
    }

    @Override
    public String toString() {
        return "TransactionData{" +
                "value=" + value +
                ", paymentType=" + paymentType +
                ", installment=" + installment +
                ", status='" + status + '\'' +
                ", brand='" + brand + '\'' +
                ", address='" + address + '\'' +
                ", sellerName='" + sellerName + '\'' +
                ", acquiring='" + acquiring + '\'' +
                ", pan='" + pan + '\'' +
                ", autoCode='" + autoCode + '\'' +
                ", documentType='" + documentType + '\'' +
                ", document='" + document + '\'' +
                ", nsu='" + nsu + '\'' +
                ", date='" + date + '\'' +
                ", hour='" + hour + '\'' +
                ", cv='" + cv + '\'' +
                ", arqc='" + arqc + '\'' +
                ", aid='" + aid + '\'' +
                ", sellerReceipt='" + sellerReceipt + '\'' +
                ", customerReceipt='" + customerReceipt + '\'' +
                ", approvalMessage='" + approvalMessage + '\'' +
                ", aidLabel='" + aidLabel + '\'' +
                ", transactionId='" + transactionId + '\'' +
                ", idPix='" + idPix + '\'' +
                '}';
    }
}
