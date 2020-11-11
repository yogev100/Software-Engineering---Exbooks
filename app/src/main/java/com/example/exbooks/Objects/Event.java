package com.example.exbooks.Objects;
import java.util.ArrayList;

public class Event {
    private int day;
    private int month;
    private int hour;
    private int min;
    private long duration;
    ArrayList<Book> books;

    public Event(ArrayList<Book> books,int day,int month,int hour,int min,long duration){
        this.books=books;
        this.day=day;
        this.month=month;
        this.hour=hour;
        this.min=min;
        this.duration=duration;
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

    public void setDuration(long duration) {
        this.duration = duration;
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
