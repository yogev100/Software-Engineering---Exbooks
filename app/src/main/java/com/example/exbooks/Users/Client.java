package com.example.exbooks.Users;
import com.example.exbooks.Objects.*;

import java.util.ArrayList;

public class Client extends User {
    ArrayList<Book> my_books;
    ArrayList<Book> donated_books;

    public Client(){}
    public Client(String fullname, String email, String password, String city, String phone) {
        super(fullname, email, password, city, phone);
        my_books=new ArrayList<Book>();
        donated_books=new ArrayList<Book>();
    }

    public ArrayList<Book> getDonated_books() {
        return donated_books;
    }

    public ArrayList<Book> getMy_books() {
        return my_books;
    }

    public void setDonated_books(ArrayList<Book> donated_books) {
        this.donated_books = donated_books;
    }

    public void setMy_books(ArrayList<Book> my_books) {
        this.my_books = my_books;
    }
}
