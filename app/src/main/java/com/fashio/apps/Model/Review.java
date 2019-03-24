package com.fashio.apps.Model;

public class Review {

    private String review;
    private String rating;
    private String name;

    public Review(String review, String rating, String name) {
        this.review = review;
        this.rating = rating;
        this.name = name;
    }

    public String getReview() {
        return review;
    }

    public String getRating() {
        return rating;
    }

    public String getName() {
        return name;
    }
}
