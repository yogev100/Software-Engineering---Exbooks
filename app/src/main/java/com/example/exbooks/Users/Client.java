package com.example.exbooks.Users;
import java.util.ArrayList;

public class Client extends User {
    ArrayList<String> my_books;
    ArrayList<String> donated_books;

    public Client(){
        my_books=new ArrayList<String>();
        donated_books=new ArrayList<String>();
    }
    public Client(String fullname, String email, String password, String city, String phone) {
        super(fullname, email, password, city, phone);
        my_books=new ArrayList<String>();
        donated_books=new ArrayList<String>();
    }

    public ArrayList<String> getDonated_books() {
        return donated_books;
    }

    public ArrayList<String> getMy_books() {
        return my_books;
    }

    public void setDonated_books(ArrayList<String> donated_books) {
        this.donated_books = donated_books;
    }

    public void setMy_books(ArrayList<String> my_books) {
        this.my_books = my_books;
    }
}
