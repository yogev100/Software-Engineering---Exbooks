package com.example.exbooks.Objects;
import java.util.ArrayList;

/**
 * This class represent a lone book event that shown if there are much books donated
 */
public class Event {
    private int day;
    private int month;
    private int hour;
    private int min;
    private final int duration = 2;
    ArrayList<Book> books;

    public Event(){}

    public Event(ArrayList<Book> books,int day,int month,int hour,int min){
        this.books=books;
        this.day=day;
        this.month=month;
        this.hour=hour;
        this.min=min;
   }

    public ArrayList<Book> getBooks() {
        return books;
    }

    public int getDay() {
        return day;
    }

    public int getHour() {
        return hour;
    }

    public int getMin() {
        return min;
    }

    public int getMonth() {
        return month;
    }

    public long getDuration() {
        return duration;
    }

    public void setBooks(ArrayList<Book> books) {
        this.books = books;
    }

    public void setDay(int day) {
        this.day = day;
    }

    public void setHour(int hour) {
        this.hour = hour;
    }

    public void setMin(int min) {
        this.min = min;
    }

    public void setMonth(int month) {
        this.month = month;
    }
}
