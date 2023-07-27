package com.bet.mpos.objects;

public class BetGame {
    public String id, game, location;

    public BetGame(String id, String game, String location) {
        this.id = id;
        this.game = game;
        this.location = location;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getGame() {
        return game;
    }

    public void setGame(String game) {
        this.game = game;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    @Override
    public String toString() {
        return "BetGame{" +
                "id='" + id + '\'' +
                ", game='" + game + '\'' +
                ", location='" + location + '\'' +
                '}';
    }
}
