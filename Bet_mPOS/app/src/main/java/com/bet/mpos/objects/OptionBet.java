package com.bet.mpos.objects;

public class OptionBet {

    private String id, name, logo, odd;

    public OptionBet(String id, String name, String logo, String odd) {
        this.id = id;
        this.name = name;
        this.logo = logo;
        this.odd = odd;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLogo() {
        return logo;
    }

    public void setLogo(String logo) {
        this.logo = logo;
    }

    public String getOdd() {
        return odd;
    }

    public void setOdd(String odd) {
        this.odd = odd;
    }

    @Override
    public String toString() {
        return "OptionBet{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", logo='" + logo + '\'' +
                ", odd='" + odd + '\'' +
                '}';
    }
}
