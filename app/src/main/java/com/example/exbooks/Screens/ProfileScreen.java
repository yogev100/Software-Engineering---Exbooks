package com.example.exbooks.Screens;

import android.os.Bundle;
import android.provider.ContactsContract;
import android.view.View;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.exbooks.Objects.Book;
import com.example.exbooks.Objects.BookFormAdapter;
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

import okhttp3.internal.cache.DiskLruCache;

public class ProfileScreen extends AppCompatActivity  implements View.OnClickListener {

    // components
//    ScrollView sv;
    Button update;
    EditText phone, name, city;
    TextView email;
    String _PHONE, _NAME, _CITY,_EMAIL;
    String UID;

    // Auth and DB references
    FirebaseAuth cAuth;
    DatabaseReference ClientRoot;
    DatabaseReference ManagerRoot;
    DatabaseReference CurrentUser;
    DatabaseReference bookRef = FirebaseDatabase.getInstance().getReference("Books");

    boolean isClient;
    Manager m;
    Client c;
    ArrayList<String> thisUserBooks;
    ArrayList<Book> bookModels;
    RecyclerView listView;

    private static ProfileBookAdapter adapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.profile_screen);

        // Buttons
        update = (Button) findViewById(R.id.updateDetailsButton);
        update.setOnClickListener(this);

        // Text
        phone = (EditText) findViewById(R.id.editTextPhone);
        name = (EditText) findViewById(R.id.editName);
        city = (EditText) findViewById(R.id.editCity);
        email = (TextView) findViewById(R.id.yourMail);

        // Database
        cAuth = FirebaseAuth.getInstance();
        ClientRoot = FirebaseDatabase.getInstance().getReference("Users").child("Clients");
        ManagerRoot = FirebaseDatabase.getInstance().getReference("Users").child("Managers");
        UID = FirebaseAuth.getInstance().getCurrentUser().getUid();

        // call to the function that shows all the data from the database and checks if this user is client or manager
        isClient();

        //Scroll view
//        sv = (ScrollView) findViewById(R.id.profile_scrollView);
        listView=(RecyclerView)findViewById(R.id.list_profile);
        LinearLayoutManager LayoutManage = new LinearLayoutManager(this);
        LayoutManage.setOrientation(LinearLayoutManager.HORIZONTAL);
        listView.setLayoutManager(LayoutManage);
        bookModels=new ArrayList<>();
        myBookListInit();

//        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                Book dataModel= bookModels.get(position);
//                Toast.makeText(ProfileScreen.this, "you click on an item", Toast.LENGTH_LONG).show();
//            }
//        });

    }

    private void myBookListInit() {
        bookRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(isClient){
                    for (int i=0; i<c.getMy_books().size();i++){
                        for (DataSnapshot category : snapshot.getChildren()){
                            Book b = category.child(c.getMy_books().get(i)).getValue(Book.class);
                            if(b!=null){
                                bookModels.add(new Book(b));
                            }
                        }
                    }
                }else{ // Manager
                    for (int i=0; i<m.getMy_books().size();i++){
                        for (DataSnapshot category : snapshot.getChildren()){
                            Book b = category.child(m.getMy_books().get(i)).getValue(Book.class);
                            if(b!=null){
                                bookModels.add(new Book(b));
                            }
                        }
                    }

                }
                adapter=new ProfileBookAdapter(bookModels,getApplicationContext());
                Adapter R = adapter;
                listView.setAdapter((RecyclerView.Adapter) R);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }


    void isClient() {
        // ClientRoot.child(UID) -> Ia there a client with this UID
        ClientRoot.child(UID).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Client client = snapshot.getValue(Client.class);
                if (client != null) {
                    isClient = true;
                    c=client;
                    _PHONE = c.getPhone();
                    _NAME = c.getfullname();
                    _CITY = c.getCity();
                    _EMAIL = c.getEmail();
                    thisUserBooks = c.getMy_books();
                    // set the data to the text fields
                    phone.setText(_PHONE);
                    city.setText(_CITY);
                    name.setText(_NAME);
                    email.setText(_EMAIL);
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
                if (manager != null) {
                    isClient = false;
                    m=manager;
                    _PHONE = m.getPhone();
                    _NAME = m.getfullname();
                    _CITY = m.getCity();
                    _EMAIL = m.getEmail();
                    thisUserBooks = m.getMy_books();
                    // set the data to the text fields
                    phone.setText(_PHONE);
                    city.setText(_CITY);
                    name.setText(_NAME);
                    email.setText(_EMAIL);
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
        // taking the perosnal information that changed-
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

    private boolean isPhoneChanged() {
        if (isClient) {
            //ClientRoot = FirebaseDatabase.getInstance().getReference().child("Users").child("Clients"); // update if its changed..
            if (!_PHONE.equals(phone.getText().toString()) && (phone.getText().toString().length()==10 || phone.getText().toString().length()==11)) {
                CurrentUser.child("phone").setValue(phone.getText().toString());
                return true;
            } else {
                return false;
            }
        } else {
            //ManagerRoot = FirebaseDatabase.getInstance().getReference().child("Users").child("Managers"); // update if its changed..
            if (!_PHONE.equals(phone.getText().toString()) && (phone.getText().toString().length()==10 || phone.getText().toString().length()==11)) {
                CurrentUser.child("phone").setValue(phone.getText().toString());
                return true;
            } else {
                return false;
            }
        }
    }

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