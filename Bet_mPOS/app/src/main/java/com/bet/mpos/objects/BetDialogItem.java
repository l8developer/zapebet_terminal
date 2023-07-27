package com.bet.mpos.objects;

public class BetDialogItem {

    private String title, subTile;
    private Double odd;
    private String document;
    private int value;

    public BetDialogItem(String title, String subTile, Double odd, String document, int value) {
        this.title = title;
        this.subTile = subTile;
        this.odd = odd;
        this.document = document;
        this.value = value;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getSubTile() {
        return subTile;
    }

    public void setSubTile(String subTile) {
        this.subTile = subTile;
    }

    public Double getOdd() {
        return odd;
    }

    public void setOdd(Double odd) {
        this.odd = odd;
    }

    public String getDocument() {
        return document;
    }

    public void setDocument(String document) {
        this.document = document;
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return "BetDialogItem{" +
                "title='" + title + '\'' +
                ", subTile='" + subTile + '\'' +
                ", odd=" + odd +
                ", document='" + document + '\'' +
                ", value=" + value +
                '}';
    }
}
