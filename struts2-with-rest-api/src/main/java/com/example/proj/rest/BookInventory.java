package com.example.proj.rest;

import com.example.proj.model.Book;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfWriter;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.sql.*;
import java.util.*;
import java.util.logging.FileHandler;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;
import java.util.stream.Collectors;

public class BookInventory {

    private final static String filePathpro="D:\\AdventNet\\MickeyLite\\webapps\\struts2-with-rest-api\\src\\main\\resources\\bookinventory.properties";
    static Properties properties = new Properties();
    private Connection connection = null;
    private List<Book> books;
    private static String filePath;

    public void addBook(Book book) throws IOException { ///need not to change

        book.setId(getBooks().size()+1);
        String sql = "INSERT INTO inventoryaccess (id, title, author, genre, publicationyear, price, quantity) VALUES (?, ?, ?, ?, ?, ?, ?)";
        try{
            connection = connectToDB();
                PreparedStatement statement = connection.prepareStatement(sql);
                statement.setInt(1, book.getId());
                statement.setString(2, book.getTitle());
                statement.setString(3, book.getAuthor());
                statement.setString(4, book.getGenre());
                statement.setInt(5,book.getPublicationYear());
                statement.setDouble(6,book.getPrice());
                statement.setInt(7,book.getQuantity());
                statement.executeUpdate();
                logreport("Title :"+book.getTitle()+" \nAuthor :"+book.getAuthor()+"\nGenre :"+book.getGenre());
        } catch (Exception e) {
            e.printStackTrace();
            logreport(e.getMessage());
        }

    }

    public void updateBook(Book book) throws SQLException {
        connection = connectToDB();
        try{
            String sql = "UPDATE inventoryaccess SET title = ?,author = ?,genre = ?,publicationyear= ?,price = ?,quantity = ? WHERE id = ?";
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setString(1, book.getTitle());
            statement.setString(2, book.getAuthor());
            statement.setString(3, book.getGenre());
            statement.setInt(4, book.getPublicationYear());
            statement.setDouble(5, book.getPrice());
            statement.setInt(6, book.getQuantity());
            statement.setInt(7, book.getId());
            statement.executeUpdate();
            logreport("Updated Book \n"+book.getAuthor()+"\n"+book.getTitle()+"\n"+book.getGenre()+"\n");
        }catch (Exception e){
            logreport(e.getMessage());
        }
    }
    public void removeBook(Book book) throws SQLException {
        connection = connectToDB();
        try{
            Statement statement = connection.createStatement();
            String sql = "DELETE FROM inventoryaccess WHERE id = "+book.getId();
            statement.executeUpdate(sql);
            statement.close();
            connection.close();
            logreport(book.getId()+" Book removed\n "+book.getTitle());
        } catch (Exception e){
            logreport(e.getMessage());
        }
    }
    public List<Book> getBooks() throws IOException{
        books = new ArrayList<>();
        String sql = "SELECT * FROM inventoryaccess";
        connection = connectToDB();
        try (Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(sql)) {
            while (resultSet.next()) {
                int id = resultSet.getInt("id");
                String title = resultSet.getString("title");
                String author = resultSet.getString("author");
                String genre = resultSet.getString("genre");
                int publicationYear = resultSet.getInt("publicationyear");
                double price = resultSet.getDouble("price");
                int quantity = resultSet.getInt("quantity");

                Book book = new Book(id, title, author, genre, publicationYear, price, quantity);
                books.add(book);
                Collections.sort(books,new Comparator<Book>(){
                    public int compare(Book book1,Book book2){
                        return Integer.compare(book1.getId(),book2.getId());
                    }
                });
            }
        } catch (SQLException e) {
            e.printStackTrace();
            logreport(e.getMessage());
        }
        return books;
    }
    public List<Book> searchBooks(SearchCriteria criteria) throws IOException {
        books = getBooks();
        logreport("Books searched......\n");
        return books.stream().filter(book -> (book.getTitle().toLowerCase().contains(criteria.getTitle().toLowerCase())) || (book.getAuthor().toLowerCase().contains(criteria.getAuthor().toLowerCase())) || (book.getPublicationYear() == criteria.getPublicationYear()) || (book.getGenre().toLowerCase().contains(criteria.getGenre().toLowerCase()))).collect(Collectors.toList());

    }

    public void generateReports() throws IOException {
        int totalBooks = books.size();
        double totalPrice = books.stream().mapToDouble(Book::getPrice).sum();
        Book bookWithHighestPrice = books.stream().max(Comparator.comparingDouble(Book::getPrice)).get();
        Book bookWithLowestPrice = books.stream().min(Comparator.comparingDouble(Book::getPrice)).get();
        double averagePrice = totalPrice / totalBooks;
        Document document = new Document(PageSize.A4);
        try {
            PdfWriter.getInstance(document, new FileOutputStream(properties.getProperty("Pdfpath")));
            document.open();
            document.add(new Paragraph("Total Books: "+books.size()+"\n"));
            document.add(new Paragraph("\n"));
            document.add(new Paragraph("\n"));
            document.add(new Paragraph("List of books:"));
            document.add(new Paragraph(""));
            document.add(new Paragraph("\n"));
            for(int i=0;i<books.size();i++){

                document.add(new Paragraph("-> Book ID:"+(books.get(i).getId())));
                document.add(new Paragraph("-> Book Title:"+(books.get(i).getTitle())));
                document.add(new Paragraph("-> Book Author:"+(books.get(i).getAuthor())));
                document.add(new Paragraph("-> Book Quantity:"+(books.get(i).getQuantity())));
                document.add(new Paragraph("-> Book Price:"+(books.get(i).getPrice())));
                document.add(new Paragraph("-----------------------------"));
            }

            document.add(new Paragraph("\n"));
            document.add(new Paragraph("Book with Highest price:"+bookWithHighestPrice.getPrice()));
            document.add(new Paragraph("Book with Lowest price:"+bookWithLowestPrice.getPrice()));
            document.add(new Paragraph("Book with Average price:"+averagePrice));
            logreport("Report Generated Successfully.............\n");
        } catch (DocumentException e) {
            throw new RuntimeException(e);
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        document.close();
        System.out.println("Report generated successfully............\n");
    }

    public Book getAccountById(int id) throws IOException {
        books = getBooks();
        logreport("Id :"+id+" fetched..../");
        return books.stream()
                .filter(book -> book.getId() == id)
                .findFirst()
                .orElse(null);
    }

    public Connection connectToDB() {
        Connection connection = null;
        try {
            properties.load(new FileReader(filePathpro));
            String URL = properties.getProperty("jdbcurl");
            Class.forName(properties.getProperty("driver"));
            connection = DriverManager.getConnection(URL, properties.getProperty("user"), properties.getProperty("password"));

        } catch (Exception e) {
        }
        return connection;
    }
    static Logger logger;

    public static void logreport(String text){
        logger = Logger.getLogger(BookInventory.class.getName());
        try {
            properties.load(new FileReader(filePathpro));
            filePath = properties.getProperty("Logpath");
            FileHandler fileHandler = new FileHandler(filePath, true);
            fileHandler.setFormatter(new SimpleFormatter());
            logger.addHandler(fileHandler);
            logger.setUseParentHandlers(false);
            logger.info(text);
        } catch (IOException e) {
            logger.warning(text);
            e.printStackTrace();
        }
    }

}
