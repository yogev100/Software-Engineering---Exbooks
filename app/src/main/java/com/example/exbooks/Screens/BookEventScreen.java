package com.example.exbooks.Screens;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.exbooks.Adapters.BookEventAdapter;
import com.example.exbooks.Objects.Book;
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

public class BookEventScreen extends AppCompatActivity implements View.OnClickListener{

    ArrayList<Book> bookModels;
    ListView listView;
    private static BookEventAdapter adapter;
    DatabaseReference books_ref;
    Button menu_btn;
    boolean is_client;
    String uid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.book_event_screen);
        isClient();
        menu_btn = (Button)findViewById(R.id.back_from_event);
        menu_btn.setOnClickListener(this);
        listView=(ListView)findViewById(R.id.event_list);

        // set the adapter to the list for show all of them in custom layout
        bookModels = new ArrayList<>();
        getEventBooks();


    }

    private void getEventBooks() {
        DatabaseReference event_uid = FirebaseDatabase.getInstance().getReference("ManagerTools").child("event_uid");
        event_uid.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                uid = snapshot.getValue(String.class);
                DatabaseReference managerEventBooksRef = FirebaseDatabase.getInstance().getReference("Users").child("Managers").child(uid);
                managerEventBooksRef.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        Manager m = snapshot.getValue(Manager.class);
                        bookModels = m.getEvent().getBooks();
                        adapter=new BookEventAdapter(bookModels,getApplicationContext(),"search",getSupportFragmentManager());
                        listView.setAdapter(adapter);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    @Override
    public void onClick(View v) {
        if (v == menu_btn){
            if(is_client){
                startActivity(new Intent(BookEventScreen.this, CustomerMenu.class));
                finish();
            }
            else{
                startActivity(new Intent(BookEventScreen.this, ManagerMenu.class));
                finish();
            }
        }
    }

    /*
    function that assign to the 'is_client' variable true when the current user is client,
    otherwise - assign false
     */
    private void isClient() {
        FirebaseAuth auth = FirebaseAuth.getInstance();
        String uid = auth.getCurrentUser().getUid();
        DatabaseReference ClientRoot = FirebaseDatabase.getInstance().getReference("Users").child("Clients");
        ClientRoot.child(uid).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Client client = snapshot.getValue(Client.class);
                if (client != null) {
                    is_client = true;
                }
                else{
                    is_client = false;
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(BookEventScreen.this, "Something was wrong!", Toast.LENGTH_LONG).show();
            }
        });

    }
}
