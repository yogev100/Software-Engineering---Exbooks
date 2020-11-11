package com.example.exbooks.Objects;
enum Condition{
    NEW,USED,TORN
}
public class  Book {
    private String book_name;
    private String author_name;
    private String category;
    private int num_pages;
    private Condition book_cond;

    public Book(String book_name, String category, String author_name, int num_pages, Condition book_cond){
        this.book_name = book_name;
        this.book_cond = book_cond;
        this.author_name = author_name;
        this.num_pages = num_pages;
        this.book_cond = book_cond;
        this.category = category;
    }

    public Condition getBook_cond() {
        return book_cond;
    }

    public String getAuthor_name() {
        return author_name;
    }

    public String getBook_name() {
        return book_name;
    }

    public int getNum_pages() {
        return num_pages;
    }

    public void setAuthor_name(String author_name) {
        this.author_name = author_name;
    }

    public void setBook_cond(Condition book_cond) {
        this.book_cond = book_cond;
    }

    public void setBook_name(String book_name) {
        this.book_name = book_name;
    }

    public void setNum_pages(int num_pages) {
        this.num_pages = num_pages;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }
}
