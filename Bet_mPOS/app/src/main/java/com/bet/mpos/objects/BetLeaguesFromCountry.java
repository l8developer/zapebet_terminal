package com.bet.mpos.objects;

import java.util.ArrayList;

public class BetLeaguesFromCountry {

    public ArrayList<League> league;
    public ArrayList<Teams> teams;

    public BetLeaguesFromCountry(ArrayList<League> league, ArrayList<Teams> teams) {
        this.league = league;
        this.teams = teams;
    }

    public ArrayList<League> getLeague() {
        return league;
    }

    public void setLeague(ArrayList<League> league) {
        this.league = league;
    }

    public ArrayList<Teams> getTeams() {
        return teams;
    }

    public void setTeams(ArrayList<Teams> teams) {
        this.teams = teams;
    }

    @Override
    public String toString() {
        return "BetLeaguesFromCountry{" +
                "league=" + league +
                ", teams=" + teams +
                '}';
    }

    public class League{
        public String name, type, logo, country_name, country_code,
                season, season_start, season_end, created_at, updated_at;

        public boolean active;
        public int id;

        public League(String name, String type, String logo, String country_name, String country_code, String season, String season_start, String season_end, String created_at, String updated_at, boolean active, int id) {
            this.name = name;
            this.type = type;
            this.logo = logo;
            this.country_name = country_name;
            this.country_code = country_code;
            this.season = season;
            this.season_start = season_start;
            this.season_end = season_end;
            this.created_at = created_at;
            this.updated_at = updated_at;
            this.active = active;
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public String getLogo() {
            return logo;
        }

        public void setLogo(String logo) {
            this.logo = logo;
        }

        public String getCountry_name() {
            return country_name;
        }

        public void setCountry_name(String country_name) {
            this.country_name = country_name;
        }

        public String getCountry_code() {
            return country_code;
        }

        public void setCountry_code(String country_code) {
            this.country_code = country_code;
        }

        public String getSeason() {
            return season;
        }

        public void setSeason(String season) {
            this.season = season;
        }

        public String getSeason_start() {
            return season_start;
        }

        public void setSeason_start(String season_start) {
            this.season_start = season_start;
        }

        public String getSeason_end() {
            return season_end;
        }

        public void setSeason_end(String season_end) {
            this.season_end = season_end;
        }

        public String getCreated_at() {
            return created_at;
        }

        public void setCreated_at(String created_at) {
            this.created_at = created_at;
        }

        public String getUpdated_at() {
            return updated_at;
        }

        public void setUpdated_at(String updated_at) {
            this.updated_at = updated_at;
        }

        public boolean isActive() {
            return active;
        }

        public void setActive(boolean active) {
            this.active = active;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        @Override
        public String toString() {
            return "League{" +
                    "name='" + name + '\'' +
                    ", type='" + type + '\'' +
                    ", logo='" + logo + '\'' +
                    ", country_name='" + country_name + '\'' +
                    ", country_code='" + country_code + '\'' +
                    ", season='" + season + '\'' +
                    ", season_start='" + season_start + '\'' +
                    ", season_end='" + season_end + '\'' +
                    ", created_at='" + created_at + '\'' +
                    ", updated_at='" + updated_at + '\'' +
                    ", active=" + active +
                    ", id=" + id +
                    '}';
        }
    }

    public class Teams{

    }
}
