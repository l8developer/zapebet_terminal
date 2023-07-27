package com.bet.mpos.objects;

public class ReportData {

    private Value soldGross, soldLiquid, installmentSales;
    private Value credit, debit, pix;

    public ReportData(Value soldGross, Value soldLiquid, Value installmentSales, Value credit, Value debit, Value pix) {
        this.soldGross = soldGross;
        this.soldLiquid = soldLiquid;
        this.installmentSales = installmentSales;
        this.credit = credit;
        this.debit = debit;
        this.pix = pix;
    }

    public Value getSoldGross() {
        return soldGross;
    }

    public void setSoldGross(Value soldGross) {
        this.soldGross = soldGross;
    }

    public Value getSoldLiquid() {
        return soldLiquid;
    }

    public void setSoldLiquid(Value soldLiquid) {
        this.soldLiquid = soldLiquid;
    }

    public Value getInstallmentSales() {
        return installmentSales;
    }

    public void setInstallmentSales(Value installmentSales) {
        this.installmentSales = installmentSales;
    }

    public Value getCredit() {
        return credit;
    }

    public void setCredit(Value credit) {
        this.credit = credit;
    }

    public Value getDebit() {
        return debit;
    }

    public void setDebit(Value debit) {
        this.debit = debit;
    }

    public Value getPix() {
        return pix;
    }

    public void setPix(Value pix) {
        this.pix = pix;
    }

    @Override
    public String toString() {
        return "ReportData{" +
                "soldGross=" + soldGross +
                ", soldLiquid=" + soldLiquid +
                ", installmentSales=" + installmentSales +
                ", credit=" + credit +
                ", debit=" + debit +
                ", pix=" + pix +
                '}';
    }

    public static class Value{

        private String value, quantity;

        public Value(String value, String quantity) {
            this.value = value;
            this.quantity = quantity;
        }

        public String getValue() {
            return value;
        }

        public String getQuantity() {
            return quantity;
        }

        @Override
        public String toString() {
            return "Value{" +
                    "value='" + value + '\'' +
                    ", quantity='" + quantity + '\'' +
                    '}';
        }
    }
}
