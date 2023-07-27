package com.bet.mpos.objects;

public class BetItem {

    private String date;

    private String id;

    private String title, location;
    private OptionBet bet0, bet1, bet2;

    public BetItem(String date, String id, String title, String location, OptionBet bet0, OptionBet bet1, OptionBet bet2) {
        this.date = date;
        this.id = id;
        this.title = title;
        this.location = location;
        this.bet0 = bet0;
        this.bet1 = bet1;
        this.bet2 = bet2;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public OptionBet getBet0() {
        return bet0;
    }

    public void setBet0(OptionBet bet0) {
        this.bet0 = bet0;
    }

    public OptionBet getBet1() {
        return bet1;
    }

    public void setBet1(OptionBet bet1) {
        this.bet1 = bet1;
    }

    public OptionBet getBet2() {
        return bet2;
    }

    public void setBet2(OptionBet bet2) {
        this.bet2 = bet2;
    }

    @Override
    public String toString() {
        return "BetItem{" +
                "date='" + date + '\'' +
                ", id=" + id + '\'' +
                ", title='" + title + '\'' +
                ", location='" + location + '\'' +
                ", bet0=" + bet0 +
                ", bet1=" + bet1 +
                ", bet2=" + bet2 +
                '}';
    }
}
