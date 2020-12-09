package com.example.exbooks.Users;
import com.example.exbooks.Objects.*;

import java.util.ArrayList;

public class Manager extends User{
    ArrayList<String> my_books;
    ArrayList<Notification> notification;

    private Event event;
    private int num_of_books_donated;

    public Manager(){
        my_books=new ArrayList<String>();
        notification=new ArrayList<Notification>();
    }

    public Manager(String fullname, String email, String password, String city, String phone) {
        super(fullname, email, password, city, phone);
        my_books=new ArrayList<String>();
        notification=new ArrayList<Notification>();

    }

    public Event getEvent() {
        return event;
    }

    public void setEvent(Event event) {
        this.event = event;
    }

    public int getNum_of_books_donated() {
        return num_of_books_donated;
    }

    public void setNum_of_books_donated(int num_of_books_donated) {
        this.num_of_books_donated = num_of_books_donated;
    }

    public ArrayList<String> getMy_books() {
        return my_books;
    }

    public void setMy_books(ArrayList<String> my_books) {
        this.my_books = my_books;
    }

    public ArrayList<Notification> getNotification() {
        return notification;
    }

    public void setNotification(ArrayList<Notification> notification) {
        this.notification = notification;
    }
}
