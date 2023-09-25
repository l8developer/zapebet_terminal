package com.bet.mpos.objects;

import java.util.ArrayList;

public class BetGamesFromLeague {
    public ArrayList<Odds> odds;
    //public Odds odds;
    public BetGamesFromLeague(ArrayList<Odds> odds) {
        this.odds = odds;
    }

    public ArrayList<Odds> getOdds() {
        return odds;
    }

    public void setOdds(ArrayList<Odds> odds) {
        this.odds = odds;
    }


//    public BetGamesFromLeague(Odds odds) {
//        this.odds = odds;
//    }
//
//    public Odds getOdds() {
//        return odds;
//    }
//
//    public void setOdds(Odds odds) {
//        this.odds = odds;
//    }

    @Override
    public String toString() {
        return "BetGamesFromLeague{" +
                "odds=" + odds +
                '}';
    }

    public class  Odds {
        public int id, league_id, venue_id, is_current;
        public String uuid, date_init, date_final_prevision, referee, round,
                    season, venue_name, created_at, updated_at, winner;

        //public boolean is_current;

        public Team home;

        public Team away;

        public ArrayList<Odd> odd;

        public Odds(int id, int league_id, int venue_id, String uuid, String date_init, String date_final_prevision, String referee, String round, String season, String venue_game, String created_at, String updated_at, String winner, int is_current, Team home, Team away, ArrayList<Odd> odd) {
            this.id = id;
            this.league_id = league_id;
            this.venue_id = venue_id;
            this.uuid = uuid;
            this.date_init = date_init;
            this.date_final_prevision = date_final_prevision;
            this.referee = referee;
            this.round = round;
            this.season = season;
            this.venue_name = venue_name;
            this.created_at = created_at;
            this.updated_at = updated_at;
            this.winner = winner;
            this.is_current = is_current;
            this.home = home;
            this.away = away;
            this.odd = odd;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public int getLeague_id() {
            return league_id;
        }

        public void setLeague_id(int league_id) {
            this.league_id = league_id;
        }

        public int getVenue_id() {
            return venue_id;
        }

        public void setVenue_id(int venue_id) {
            this.venue_id = venue_id;
        }

        public String getUuid() {
            return uuid;
        }

        public void setUuid(String uuid) {
            this.uuid = uuid;
        }

        public String getDate_init() {
            return date_init;
        }

        public void setDate_init(String date_init) {
            this.date_init = date_init;
        }

        public String getDate_final_prevision() {
            return date_final_prevision;
        }

        public void setDate_final_prevision(String date_final_prevision) {
            this.date_final_prevision = date_final_prevision;
        }

        public String getReferee() {
            return referee;
        }

        public void setReferee(String referee) {
            this.referee = referee;
        }

        public String getRound() {
            return round;
        }

        public void setRound(String round) {
            this.round = round;
        }

        public String getSeason() {
            return season;
        }

        public void setSeason(String season) {
            this.season = season;
        }

        public String getVenue_name() {
            return venue_name;
        }

        public void setVenue_name(String venue_name) {
            this.venue_name = venue_name;
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

        public String getWinner() {
            return winner;
        }

        public void setWinner(String winner) {
            this.winner = winner;
        }

        public int isIs_current() {
            return is_current;
        }

        public void setIs_current(int is_current) {
            this.is_current = is_current;
        }

        public Team getHome() {
            return home;
        }

        public void setHome(Team home) {
            this.home = home;
        }

        public Team getAway() {
            return away;
        }

        public void setAway(Team away) {
            this.away = away;
        }

        public ArrayList<Odd> getOdd() {
            return odd;
        }

        public void setOdd(ArrayList<Odd> odd) {
            this.odd = odd;
        }

        @Override
        public String toString() {
            return "Odds{" +
                    "id=" + id +
                    ", league_id=" + league_id +
                    ", venue_id=" + venue_id +
                    ", uuid='" + uuid + '\'' +
                    ", date_init='" + date_init + '\'' +
                    ", date_final_prevision='" + date_final_prevision + '\'' +
                    ", referee='" + referee + '\'' +
                    ", round='" + round + '\'' +
                    ", season='" + season + '\'' +
                    ", venue_game='" + venue_name + '\'' +
                    ", created_at='" + created_at + '\'' +
                    ", updated_at='" + updated_at + '\'' +
                    ", winner='" + winner + '\'' +
                    ", is_current=" + is_current +
                    ", home=" + home +
                    ", away=" + away +
                    ", odd=" + odd +
                    '}';
        }

        public class Team{
            public int id, founded;
            public String name, code, country, logo, logo_s3,
                        created_at, updated_at;

            public boolean national;

            public Team(int id, int founded, String name, String code, String country, String logo, String logo_s3, String created_at, String updated_at, boolean national) {
                this.id = id;
                this.founded = founded;
                this.name = name;
                this.code = code;
                this.country = country;
                this.logo = logo;
                this.logo_s3 = logo_s3;
                this.created_at = created_at;
                this.updated_at = updated_at;
                this.national = national;
            }

            public int getId() {
                return id;
            }

            public void setId(int id) {
                this.id = id;
            }

            public int getFounded() {
                return founded;
            }

            public void setFounded(int founded) {
                this.founded = founded;
            }

            public String getName() {
                return name;
            }

            public void setName(String name) {
                this.name = name;
            }

            public String getCode() {
                return code;
            }

            public void setCode(String code) {
                this.code = code;
            }

            public String getCountry() {
                return country;
            }

            public void setCountry(String country) {
                this.country = country;
            }

            public String getLogo() {
                return logo;
            }

            public void setLogo(String logo) {
                this.logo = logo;
            }

            public String getLogo_s3() {
                return logo_s3;
            }

            public void setLogo_s3(String logo_s3) {
                this.logo_s3 = logo_s3;
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

            public boolean isNational() {
                return national;
            }

            public void setNational(boolean national) {
                this.national = national;
            }

            @Override
            public String toString() {
                return "Team{" +
                        "id=" + id +
                        ", founded=" + founded +
                        ", name='" + name + '\'' +
                        ", code='" + code + '\'' +
                        ", country='" + country + '\'' +
                        ", logo='" + logo + '\'' +
                        ", logo_s3='" + logo_s3 + '\'' +
                        ", created_at='" + created_at + '\'' +
                        ", updated_at='" + updated_at + '\'' +
                        ", national=" + national +
                        '}';
            }
        }

        public class Odd {
            public int id, bet_id, league_id, fixture_id, bookmaker_id;
            public String value, created_at, updated_at;
            public double odd;

            public Odd(int id, int bet_id, int league_id, int fixture_id, int bookmaker_id, String value, String created_at, String updated_at, double odd) {
                this.id = id;
                this.bet_id = bet_id;
                this.league_id = league_id;
                this.fixture_id = fixture_id;
                this.bookmaker_id = bookmaker_id;
                this.value = value;
                this.created_at = created_at;
                this.updated_at = updated_at;
                this.odd = odd;
            }

            public int getId() {
                return id;
            }

            public void setId(int id) {
                this.id = id;
            }

            public int getBet_id() {
                return bet_id;
            }

            public void setBet_id(int bet_id) {
                this.bet_id = bet_id;
            }

            public int getLeague_id() {
                return league_id;
            }

            public void setLeague_id(int league_id) {
                this.league_id = league_id;
            }

            public int getFixture_id() {
                return fixture_id;
            }

            public void setFixture_id(int fixture_id) {
                this.fixture_id = fixture_id;
            }

            public int getBookmaker_id() {
                return bookmaker_id;
            }

            public void setBookmaker_id(int bookmaker_id) {
                this.bookmaker_id = bookmaker_id;
            }

            public String getValue() {
                return value;
            }

            public void setValue(String value) {
                this.value = value;
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

            public double getOdd() {
                return odd;
            }

            public void setOdd(double odd) {
                this.odd = odd;
            }

            @Override
            public String toString() {
                return "Odd{" +
                        "id=" + id +
                        ", bet_id=" + bet_id +
                        ", league_id=" + league_id +
                        ", fixture_id=" + fixture_id +
                        ", bookmaker_id=" + bookmaker_id +
                        ", value='" + value + '\'' +
                        ", created_at='" + created_at + '\'' +
                        ", updated_at='" + updated_at + '\'' +
                        ", odd=" + odd +
                        '}';
            }
        }
    }
}
