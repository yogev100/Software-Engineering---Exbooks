package com.example.exbooks.Users;
import com.example.exbooks.Objects.*;

public class Manager extends User{

    private Event event;
    private int num_of_books_donated;

    public Manager(String username, String fname, String lname, String email, String password, String city, String phone) {
        super(username, fname, lname, email, password, city, phone);
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
}
