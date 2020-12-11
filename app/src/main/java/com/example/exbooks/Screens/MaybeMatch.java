package com.example.exbooks.Screens;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.exbooks.Objects.Book;
import com.example.exbooks.Objects.BookFormAdapter;
import com.example.exbooks.R;
import com.example.exbooks.Users.Client;
import com.example.exbooks.Users.Manager;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import android.os.Bundle;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

public class MaybeMatch extends AppCompatActivity {
    String wanterId;
    ArrayList<Book> bookModels;
    ListView listView;
    TextView exchange;
    private static BookFormAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maybe_match);
        wanterId = getIntent().getStringExtra("wanterID");
        exchange=(TextView)findViewById(R.id.maybe_textview);
        listView=(ListView)findViewById(R.id.list_of_maybe_books);
        bookModels=new ArrayList<>();
        FindCorrectBooks();



    }

    private void FindCorrectBooks() {
        DatabaseReference mRef= FirebaseDatabase.getInstance().getReference("Users").child("Managers");
        DatabaseReference cRef= FirebaseDatabase.getInstance().getReference("Users").child("Clients");

        mRef.child(wanterId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Manager m = snapshot.getValue(Manager.class);
                if(m!=null){
                    getTheUserBooksByArray(m.getMy_books());
                    exchange.setText("Books of "+m.getfullname()+" :");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        cRef.child(wanterId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Client c = snapshot.getValue(Client.class);
                if(c!=null){
                    getTheUserBooksByArray(c.getMy_books());
                    exchange.setText("Books of "+c.getfullname()+" :");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    private void getTheUserBooksByArray(final ArrayList<String> my_books) {
        DatabaseReference bookRef = FirebaseDatabase.getInstance().getReference("Books");

        bookRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot category:snapshot.getChildren()){
                    for(DataSnapshot book:category.getChildren()){
                        Book b = book.getValue(Book.class);
                        if(b!=null) {
                            for (String a : my_books) {
                                if(b.getBook_id().equals(a)&&b.isFor_change()){
                                    bookModels.add(new Book(b));
                                }
                            }
                        }
                    }
                }
                adapter=new BookFormAdapter(bookModels,getApplicationContext(),"match", getSupportFragmentManager());
                listView.setAdapter(adapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }


}