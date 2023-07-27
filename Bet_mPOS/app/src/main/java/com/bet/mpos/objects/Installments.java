package com.bet.mpos.objects;

public class Installments {
    private int installment;
    private String totalFees, installmentValue;

    public Installments() {
    }

    public Installments(int installment, String totalFees, String installmentValue) {
        this.installment = installment;
        this.totalFees = totalFees;
        this.installmentValue = installmentValue;
    }

    public int getInstallment() {
        return installment;
    }

    public void setInstallment(int installment) {
        this.installment = installment;
    }

    public String getTotalFees() {
        return totalFees;
    }

    public void setTotalFees(String totalFees) {
        this.totalFees = totalFees;
    }

    public String getInstallmentValue() {
        return installmentValue;
    }

    public void setInstallmentValu(String installmentValu) {
        this.installmentValue = installmentValu;
    }

    @Override
    public String toString() {
        return "installments{" +
                "installment=" + installment +
                ", totalFees='" + totalFees + '\'' +
                ", installmentValue='" + installmentValue + '\'' +
                '}';
    }
}
