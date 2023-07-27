package com.bet.mpos.objects;

public class ReportSalesData {

    private int transaction_type, status, value_gross, value_net, total_installments;
    private String hour, period, entry_date, card_last_dig, card_flag, transaction_id;

    public ReportSalesData(int transaction_type, int status, int value_gross, int value_net, int total_installments, String hour, String period, String entry_date, String card_last_dig, String card_flag, String transaction_id) {
        this.transaction_type = transaction_type;
        this.status = status;
        this.value_gross = value_gross;
        this.value_net = value_net;
        this.total_installments = total_installments;
        this.hour = hour;
        this.period = period;
        this.entry_date = entry_date;
        this.card_last_dig = card_last_dig;
        this.card_flag = card_flag;
        this.transaction_id = transaction_id;
    }

    public int getTransaction_type() {
        return transaction_type;
    }

    public void setTransaction_type(int transaction_type) {
        this.transaction_type = transaction_type;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public int getValue_gross() {
        return value_gross;
    }

    public void setValue_gross(int value_gross) {
        this.value_gross = value_gross;
    }

    public int getValue_net() {
        return value_net;
    }

    public void setValue_net(int value_net) {
        this.value_net = value_net;
    }

    public int getTotal_installments() {
        return total_installments;
    }

    public void setTotal_installments(int total_installments) {
        this.total_installments = total_installments;
    }

    public String getHour() {
        return hour;
    }

    public void setHour(String hour) {
        this.hour = hour;
    }

    public String getPeriod() {
        return period;
    }

    public void setPeriod(String period) {
        this.period = period;
    }

    public String getEntry_date() {
        return entry_date;
    }

    public void setEntry_date(String entry_date) {
        this.entry_date = entry_date;
    }

    public String getCard_last_dig() {
        return card_last_dig;
    }

    public void setCard_last_dig(String card_last_dig) {
        this.card_last_dig = card_last_dig;
    }

    public String getCard_flag() {
        return card_flag;
    }

    public void setCard_flag(String card_flag) {
        this.card_flag = card_flag;
    }

    public String getTransaction_id() {
        return transaction_id;
    }

    public void setTransaction_id(String transaction_id) {
        this.transaction_id = transaction_id;
    }

    @Override
    public String toString() {
        return "ReportSalesData{" +
                "transaction_type=" + transaction_type +
                ", status=" + status +
                ", value_gross=" + value_gross +
                ", value_net=" + value_net +
                ", total_installments=" + total_installments +
                ", hour='" + hour + '\'' +
                ", period='" + period + '\'' +
                ", entry_date='" + entry_date + '\'' +
                ", card_last_dig='" + card_last_dig + '\'' +
                ", card_flag='" + card_flag + '\'' +
                ", transaction_id='" + transaction_id + '\'' +
                '}';
    }
}
