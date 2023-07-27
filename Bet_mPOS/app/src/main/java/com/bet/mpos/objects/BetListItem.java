package com.bet.mpos.objects;

import java.util.ArrayList;

public class BetListItem {

    private ArrayList<BetItem> betItem;

    public BetListItem(ArrayList<BetItem> betItem) {
        this.betItem = betItem;
    }

    public ArrayList<BetItem> getBetItem() {
        return betItem;
    }

    public void setBetItem(ArrayList<BetItem> betItem) {
        this.betItem = betItem;
    }

    @Override
    public String toString() {
        return "BetListItem{" +
                "betItem=" + betItem +
                '}';
    }
}
