package com.fashio.apps.Model;

public class Ranking {

    private String ranking;
    private String seller_name;
    private String seller_sold;
    private String seller_earned;

    public Ranking(String ranking, String seller_name, String seller_sold,String seller_earned) {
        this.ranking = ranking;
        this.seller_name = seller_name;
        this.seller_sold = seller_sold;
        this.seller_earned = seller_earned;
    }

    public String getRanking() {
        return ranking;
    }

    public String getSeller_name() {
        return seller_name;
    }

    public String getSeller_earned() {
        return seller_earned;
    }

    public String getSeller_sold() {
        return seller_sold;
    }
}

