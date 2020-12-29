package com.example.exbooks.Users;
import com.example.exbooks.Objects.Notification;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;


/**
 * This class extends from User class and represent a Client,
 * there are few methods (getters and setters) that will help us to use with the firebase
 */
public class Client extends User {
    ArrayList<String> my_books;//field for save all books the client uploaded
    ArrayList<Notification> notification; // field for save all notifications and show them to the client
    String uid;
    DatabaseReference cRef= FirebaseDatabase.getInstance().getReference("Users").child("Clients");

    // default constructor for firebase
    public Client(){
        my_books=new ArrayList<String>();
        notification=new ArrayList<Notification>();
    }
    //arguments constructor for register
    public Client(String fullname, String email, String password, String city, String phone) {
        super(fullname, email, password, city, phone);
        my_books=new ArrayList<String>();
        notification=new ArrayList<Notification>();
    }

    // copy constructor for firebase
    public Client(Client other){
        super(other);
        this.my_books = other.getMy_books();
        this.notification = other.getNotification();

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

    public String getUid() { return uid; }

    public void setNotification(ArrayList<Notification> notification) {
        this.notification = notification;
    }

    public void setUid(String uid){ this.uid=uid; }
}
