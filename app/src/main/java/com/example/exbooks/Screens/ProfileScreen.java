package com.example.exbooks.Screens;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.exbooks.Objects.Book;
import com.example.exbooks.Objects.ProfileBookAdapter;
import com.example.exbooks.R;
import com.example.exbooks.Users.Client;
import com.example.exbooks.Users.Manager;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class ProfileScreen extends AppCompatActivity  implements View.OnClickListener {

    // components
    Button update;
    EditText phone, name, city;
    TextView email;
    String _PHONE, _NAME, _CITY,_EMAIL;
    String UID;
    ListView listView;

    // Auth and DB references
    FirebaseAuth cAuth;
    DatabaseReference ClientRoot;
    DatabaseReference ManagerRoot;
    DatabaseReference CurrentUser;
    DatabaseReference bookRef = FirebaseDatabase.getInstance().getReference("Books");

    // Variables
    boolean isClient;
    Manager m;
    Client c;
    ArrayList<String> thisUserBooks;
    ArrayList<Book> bookModels;

    // Adapter
    private static ProfileBookAdapter adapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.profile_screen);

        // Database
        cAuth = FirebaseAuth.getInstance();
        ClientRoot = FirebaseDatabase.getInstance().getReference("Users").child("Clients");
        ManagerRoot = FirebaseDatabase.getInstance().getReference("Users").child("Managers");
        UID = FirebaseAuth.getInstance().getCurrentUser().getUid();

        // call to the function that shows all the data from the database and checks if this user is client or manager
        isClient();

        // Buttons
        update = (Button) findViewById(R.id.updateDetailsButton);
        update.setOnClickListener(this);

        // Text
        phone = (EditText) findViewById(R.id.editTextPhone);
        name = (EditText) findViewById(R.id.editName);
        city = (EditText) findViewById(R.id.editCity);
        email = (TextView) findViewById(R.id.yourMail);


        // listView and Layout
        listView=(ListView) findViewById(R.id.list_profile);
        LinearLayoutManager LayoutManage = new LinearLayoutManager(this);
        LayoutManage.setOrientation(LinearLayoutManager.HORIZONTAL);

        // init array list of books
        bookModels=new ArrayList<>();
        myBookListInit();
    }

    // Method that init the book list.
    private void myBookListInit() {
        bookRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (isClient) { //Client
                    // go all over the books, and check- if its not null, go all over the categories-
                    // and add the books to the list.
                    for (int i = 0; i < c.getMy_books().size(); i++) {
                        if (c.getMy_books().get(i) != null) {
                            for (DataSnapshot category : snapshot.getChildren()) {
                                Book b = category.child(c.getMy_books().get(i)).getValue(Book.class);
                                if (b != null) {
                                    bookModels.add(new Book(b));
                                }
                            }
                        }
                    }
                }
                // turn the adapter on and set its on the listview.
                adapter = new ProfileBookAdapter(bookModels, ProfileScreen.this);
                listView.setAdapter(adapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });

        bookRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (!isClient) { // Manager
                    // go all over the books, and check- if its not null, go all over the categories-
                    // and add the books to the list.
                    for (int i = 0; i < m.getMy_books().size(); i++) {
                        if (m.getMy_books().get(i) != null) {
                            for (DataSnapshot category : snapshot.getChildren()) {
                                Book b = category.child(m.getMy_books().get(i)).getValue(Book.class);
                                if (b != null) {
                                    bookModels.add(new Book(b));
                                }
                            }
                        }
                    }
                }
                // turn the adapter on and set its on the listview.
                adapter = new ProfileBookAdapter(bookModels, ProfileScreen.this);
                listView.setAdapter(adapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }

    // isClient method checks if the user is client or manager
    void isClient() {
        // ClientRoot.child(UID) -> Ia there a client with this UID
        ClientRoot.child(UID).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Client client = snapshot.getValue(Client.class);
                // if its client-
                if (client != null) {
                    isClient = true;                            // update the boolean
                    c=new Client(client);                       // make a new client for the "work  on"
                    _PHONE = c.getPhone();                      // take the user parameters from the DB
                    _NAME = c.getfullname();
                    _CITY = c.getCity();
                    _EMAIL = c.getEmail();
                    thisUserBooks = c.getMy_books();

                    // set the data to the text fields
                    phone.setText(_PHONE);                      // set the parameters to the TextView/EditText fields
                    city.setText(_CITY);
                    name.setText(_NAME);
                    email.setText(_EMAIL);

                    // update the current user to be client.
                    CurrentUser=FirebaseDatabase.getInstance().getReference("Users").child("Clients").child(UID);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(ProfileScreen.this, "Something was wrong!", Toast.LENGTH_LONG);
            }
        });

        ManagerRoot.child(UID).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Manager manager = snapshot.getValue(Manager.class);
                // if its manager-
                if (manager != null) {
                    isClient = false;                           // update the boolean
                    m=new Manager(manager);                     // make a new client for the "work  on"
                    _PHONE = m.getPhone();                      // take the user parameters from the DB
                    _NAME = m.getfullname();
                    _CITY = m.getCity();
                    _EMAIL = m.getEmail();
                    thisUserBooks = m.getMy_books();

                    // set the data to the text fields
                    phone.setText(_PHONE);                      // set the parameters to the TextView/EditText fields
                    city.setText(_CITY);
                    name.setText(_NAME);
                    email.setText(_EMAIL);

                    // update the current user to be manager.
                    CurrentUser=FirebaseDatabase.getInstance().getReference("Users").child("Managers").child(UID);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(ProfileScreen.this, "Something was wrong!", Toast.LENGTH_LONG);
            }
        });
    }

    @Override
    public void onClick(View view) {
        // taking the personal information that changed-
        if (view == update) {
            if(!isNameChanged() && !isPhoneChanged() && !isCityChanged()){
                Toast.makeText(ProfileScreen.this, "the data is not changed", Toast.LENGTH_LONG).show();
            }
            if (isNameChanged()){
                _NAME = name.getText().toString();
                Toast.makeText(ProfileScreen.this, "The personal information updated!", Toast.LENGTH_LONG).show();
            }
            if(isPhoneChanged()){
                _PHONE = phone.getText().toString();
                Toast.makeText(ProfileScreen.this, "The personal information updated!", Toast.LENGTH_LONG).show();
            }
            if(isCityChanged()){
                _CITY = city.getText().toString();
                Toast.makeText(ProfileScreen.this, "The personal information updated!", Toast.LENGTH_LONG).show();
            }
        }
    }

    // isPhoneChanged method checks if the user's phone changed by comparison between DB and EditText field
    private boolean isPhoneChanged() {
        if (isClient) {
            if (!_PHONE.equals(phone.getText().toString()) && (phone.getText().toString().length()==10 || phone.getText().toString().length()==11)) {
                CurrentUser.child("phone").setValue(phone.getText().toString());
                return true;
            } else {
                return false;
            }
        } else {
            if (!_PHONE.equals(phone.getText().toString()) && (phone.getText().toString().length()==10 || phone.getText().toString().length()==11)) {
                CurrentUser.child("phone").setValue(phone.getText().toString());
                return true;
            } else {
                return false;
            }
        }
    }

    // isCityChanged method checks if the user's phone changed by comparison between DB and EditText field
    private boolean isCityChanged() {
        if (isClient) {
            //ClientRoot = FirebaseDatabase.getInstance().getReference().child("Users").child("Clients"); // update if its changed..
            if (!_CITY.equals(city.getText().toString()) && !city.getText().toString().isEmpty()) {
                CurrentUser.child("city").setValue(city.getText().toString());
                return true;
            } else {
                return false;
            }
        } else {
            //ManagerRoot = FirebaseDatabase.getInstance().getReference().child("Users").child("Managers"); // update if its changed..
            if (!_CITY.equals(city.getText().toString()) && !city.getText().toString().isEmpty()) {
                CurrentUser.child("city").setValue(city.getText().toString());
                return true;
            } else {
                return false;
            }
        }
    }

    // isNameChanged method checks if the user's phone changed by comparison between DB and EditText field
    private boolean isNameChanged() {
        if (isClient) {
            //ClientRoot = FirebaseDatabase.getInstance().getReference().child("Users").child("Clients"); // update if its changed..
            System.out.println(name.getText().toString());
            if (!_NAME.equals(name.getText().toString()) && !name.getText().toString().isEmpty())  {
                CurrentUser.child("fullname").setValue(name.getText().toString());
                return true;
            } else {
                return false;
            }
        } else {
            //ManagerRoot = FirebaseDatabase.getInstance().getReference().child("Users").child("Managers"); // update if its changed..
            if (!_NAME.equals(name.getText().toString()) && !name.getText().toString().isEmpty()) {
                CurrentUser.child("fullname").setValue(name.getText().toString());
                return true;
            } else {
                return false;
            }
        }
    }

}