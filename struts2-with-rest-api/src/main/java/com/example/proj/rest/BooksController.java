package com.example.proj.rest;

import com.example.proj.model.Book;
import com.opensymphony.xwork2.ActionSupport;
import com.opensymphony.xwork2.ModelDriven;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import org.apache.struts2.rest.DefaultHttpHeaders;
import org.apache.struts2.rest.HttpHeaders;

public class BooksController extends ActionSupport  implements ModelDriven<Object>{

    public Book getBook() {
        return book;
    }

    public void setBook(Book book) {
        this.book = book;
    }

    private Book book ;

    public int getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    private String title;
    public void setId(int id) {
        this.id = id;
    }
    public int getTotalBooks() {
        return totalBooks;
    }

    public void setTotalBooks(int totalBooks) {
        this.totalBooks = totalBooks;
    }

    private int totalBooks;
    private double maxPrice;

    public double getMaxPrice() {
        return maxPrice;
    }

    public void setMaxPrice(double maxPrice) {
        this.maxPrice = maxPrice;
    }

    public double getMinPrice() {
        return minPrice;
    }

    public void setMinPrice(double minPrice) {
        this.minPrice = minPrice;
    }

    private int totalBookquantity;
    private double minPrice;
    private double totalPrice;

    private Book bookWithHighestPrice;
    private Book bookWithLowestPrice;
    private double averagePrice;

    public int getTotalBookquantity() {
        return totalBookquantity;
    }

    public void setTotalBookquantity(int totalBookquantity) {
        this.totalBookquantity = totalBookquantity;
    }

    public double getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(double totalPrice) {
        this.totalPrice = totalPrice;
    }

    public Book getBookWithHighestPrice() {
        return bookWithHighestPrice;
    }

    public void setBookWithHighestPrice(Book bookWithHighestPrice) {
        this.bookWithHighestPrice = bookWithHighestPrice;
    }

    public Book getBookWithLowestPrice() {
        return bookWithLowestPrice;
    }

    public void setBookWithLowestPrice(Book bookWithLowestPrice) {
        this.bookWithLowestPrice = bookWithLowestPrice;
    }

    public double getAveragePrice() {
        return averagePrice;
    }

    public void setAveragePrice(double averagePrice) {
        this.averagePrice = averagePrice;
    }
    private int id;
    private String author;
    private String genre;
    private int publicationYear;
    private double price;
    private int quantity;

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getGenre() {
        return genre;
    }

    public void setGenre(String genre) {
        this.genre = genre;
    }

    public int getPublicationYear() {
        return publicationYear;
    }

    public void setPublicationYear(int publicationYear) {
        this.publicationYear = publicationYear;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }
    public int getQuantity() {
        return quantity;
    }
    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }
    public BookInventory getBookInventory() {
        return bookInventory;
    }
    public void setBookInventory(BookInventory bookInventory) {
        this.bookInventory = bookInventory;
    }
    public String getSearch() {
        return search;
    }
    public void setSearch(String search) {
        this.search = search;
    }
    private String search;
    public SearchCriteria getCriteria() {
        return criteria;
    }
    public void setCriteria(SearchCriteria criteria) {
        this.criteria = criteria;
    }
    private List<Book> books;
    private BookInventory bookInventory = new BookInventory();

    private SearchCriteria criteria;
    public List<Book> getBooks() {
        return books;
    }
    public void setBooks(List<Book> books) {
        this.books = books;
    }

    //GET /api/accounts
    public HttpHeaders index() throws IOException {
        try{
            try {
                criteria = new SearchCriteria(search, search, search, Integer.parseInt(search), Integer.parseInt(search), Integer.parseInt(search));
            } catch (Exception e) {
                criteria = new SearchCriteria(search, search, search);
            }
            books = bookInventory.searchBooks(criteria);
        }catch (Exception e){
            books = bookInventory.getBooks();
        }
        return new DefaultHttpHeaders("index").disableCaching();
    }

    //GET /api/accounts/{id}
    public HttpHeaders show() throws IOException {
        books = new ArrayList<>();
        System.out.println(getId());
        book = bookInventory.getAccountById(getId());
        books.add(book);
        return new DefaultHttpHeaders("show");
    }

    //POST /api/accounts
    public HttpHeaders create() throws Exception {
        System.out.println(getTitle());
        Book book = new Book(title,author,genre,publicationYear,price,quantity);
        bookInventory.addBook(book);
        return new DefaultHttpHeaders("create");
    }

    //DELETE /api/accounts/{id}
    public HttpHeaders destroy() throws Exception {
        System.out.println("DELETE \t /account:" +getId());
        String result = "destroy";
        book = bookInventory.getAccountById(getId());
        System.out.println(book.getAuthor());
        bookInventory.removeBook(book);
        return new DefaultHttpHeaders(result);
    }

    //PUT /api/accounts/{id} 
    public HttpHeaders update() throws Exception{
        bookInventory = new BookInventory();
        System.out.println("PUT \t /account" +getId());
        System.out.println(title);
        System.out.println(author);
        System.out.println(quantity);
        books = new ArrayList<>();
        book = bookInventory.getAccountById(getId());
        books.add(book);
        if (book != null) {
            if (title != null) {
                book.setTitle(title);
            }
            if (author != null) {
                book.setAuthor(author);
            }
            if(genre != null){
                book.setGenre(genre);
            }
            if(publicationYear!=0){
                book.setPublicationYear(publicationYear);
            }
            if (price!=0){
                book.setPrice(price);
            }if (quantity!=0){
                book.setQuantity(quantity);
            }
            bookInventory.updateBook(book);
        }
        return new DefaultHttpHeaders("update");
    }

    //GET  /generateReport
    public String generateReport() throws IOException {
        System.out.println("Generated report");
        books = bookInventory.getBooks();
        totalBooks = books.size();
        totalPrice = books.stream().mapToDouble(Book::getPrice).sum();
        bookWithHighestPrice = books.stream().max(Comparator.comparingDouble(Book::getPrice)).get();
        bookWithLowestPrice = books.stream().min(Comparator.comparingDouble(Book::getPrice)).get();
        averagePrice = totalPrice / totalBooks;
        for(Book book2:books){
            totalBookquantity+=book2.getQuantity();
        }
        bookInventory.generateReports();
        return SUCCESS;
    }

    @Override
    public Object getModel() {
        return books;
    }
}
