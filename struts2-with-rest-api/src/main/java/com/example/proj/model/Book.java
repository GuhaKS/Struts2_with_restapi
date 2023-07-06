package com.example.proj.model;

import org.apache.commons.lang3.builder.ToStringBuilder;

public class Book {

    private int id;


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public int getPublicationYear() {
        return publicationYear;
    }

    public void setPublicationYear(int publicationYear) {
        this.publicationYear = publicationYear;
    }

    public double getPrice(){
        return this.price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public int getQuantity() {
        return this.quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public String getGenre() {
        return genre;
    }

    public void setGenre(String genre) {
        this.genre = genre;
    }

    private String title;
    private String author;
    private int publicationYear;
    private double price;
    private int quantity;
    private String genre;

    public Book() {
    }

    public Book(String title, String author, String genre,int publicationYear, double price, int quantity) {
        this.title = title;
        this.author = author;
        this.genre = genre;
        this.publicationYear = publicationYear;
        this.price = price;
        this.quantity = quantity;
    }

    public Book(int id, String title, String author,String genre, int publicationYear, double price, int quantity) {
        this.id = id;
        this.title = title;
        this.author = author;
        this.genre = genre;
        this.publicationYear = publicationYear;
        this.price = price;
        this.quantity = quantity;
    }

    // Getters and setters...

    public String toInventoryString() {
        return id + "\t" + title + "\t\t" + author + "\t\t" +genre+ " \t\t "+ publicationYear + "\t\t" + price + "\t" + quantity;
    }
    public String toFileString() {
        return id + "," + title + "," + author + "," +genre+","+ publicationYear + "," + price + "," + quantity;
    }
    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .append("id", id)
                .append("title", title)
                .append("author", author)
                .append("publicationYear",publicationYear)
                .append("price", price)
                .append("quantity", quantity)
                .append("genre",genre)
                .toString();
    }
}

