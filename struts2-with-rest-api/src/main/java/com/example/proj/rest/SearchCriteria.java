package com.example.proj.rest;


class SearchCriteria {
    private String title;
    private String author;
    private  String genre;
    private int publicationYear;
    private double minPrice;
    private double maxPrice;

    public double getPricep() {
        return pricep;
    }

    public void setPricep(double pricep) {
        this.pricep = pricep;
    }

    private double pricep;

    public SearchCriteria(String title,String author,String genre){
        this.title = title;
        this.author = author;
        this.genre = genre;
    }
    public SearchCriteria(){

    }
    public SearchCriteria(String title, String author,String genre, int publicationYear, double minPrice, double maxPrice) {
        this.title = title;
        this.author = author;
        this.genre = genre;
        this.publicationYear = publicationYear;
        this.minPrice = minPrice;
        this.maxPrice = maxPrice;
    }

    public String getTitle() {
        return title;
    }

    public String getGenre() {
        //System.out.println(genre);
        return genre;

    }

    public String getAuthor() {
        return author;
    }

    public int getPublicationYear() {
        return publicationYear;

    }

    public double getMinPrice() {
        return minPrice;
    }

    public double getMaxPrice() {
        return maxPrice;
    }
}