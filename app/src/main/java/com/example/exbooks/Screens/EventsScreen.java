package com.example.exbooks.Screens;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.exbooks.Adapters.EventAdapter;
import com.example.exbooks.Objects.Book;
import com.example.exbooks.Objects.Event;
import com.example.exbooks.R;
import com.example.exbooks.Users.Manager;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

/**
 * This class represents the screen of the donated books events,
 * the methods can find the number of donated books and make an event.
 */
public class EventsScreen extends AppCompatActivity implements View.OnClickListener{

    ArrayList<Book> bookModels;
    ListView listView;
    EditText month,day,hour,minute;
    private static EventAdapter adapter;
    private DatabaseReference books_ref;
    private FirebaseAuth managerAuth;
    private DatabaseReference managerRef;
    Button create;
    final int MinNumBooksForEvent = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.event_screen);
        books_ref = FirebaseDatabase.getInstance().getReference("Books");
        managerAuth = FirebaseAuth.getInstance();
        managerRef = FirebaseDatabase.getInstance().getReference("Users").child("Managers");
        listView=(ListView)findViewById(R.id.list_event);
        bookModels=new ArrayList<>();
        month = (EditText)findViewById(R.id.month);
        day = (EditText)findViewById(R.id.day);
        hour = (EditText)findViewById(R.id.hour);
        minute = (EditText)findViewById(R.id.minute);
        create = (Button)findViewById(R.id.create_event_button);
        create.setOnClickListener(this);

        ShowDonateBooks();

    }

    // method that set the donated books to the list, and show them by adapter.
    private void ShowDonateBooks() {
        books_ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                // go all over the books, and check if its not null and not for change = for donate. add to the list.
                for (DataSnapshot category:snapshot.getChildren()){
                    for (DataSnapshot book : category.getChildren()){
                        Book b=book.getValue(Book.class);
                        if(b !=null && !b.isFor_change()){
                            bookModels.add(new Book(b));
                        }
                    }
                }
                adapter=new EventAdapter(bookModels,getApplicationContext());
                listView.setAdapter(adapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    @Override
    public void onClick(View v) {
        ArrayList<Book> selected_books = new ArrayList<>();
        if(v==create){      // create an event.
            String monthh = month.getText().toString();
            String dayy = day.getText().toString();
            String hourr = hour.getText().toString();
            String minutee = minute.getText().toString();

            // validation checks
            if(monthh.isEmpty()){
                month.setError("You must fill the month!");
                month.requestFocus();
                return;
            }
            if(dayy.isEmpty()){
                day.setError("You must fill the day!");
                day.requestFocus();
                return;
            }
            if(hourr.isEmpty()){
                hour.setError("You must fill the hour!");
                hour.requestFocus();
                return;
            }
            if(minutee.isEmpty()){
                minute.setError("You must fill the minute!");
                minute.requestFocus();
                return;
            }
            int Month = Integer.parseInt(month.getText().toString());
            int Day = Integer.parseInt(day.getText().toString());
            int Hour = Integer.parseInt(hour.getText().toString());
            int Minute = Integer.parseInt(minute.getText().toString());

            if((Month>12 || Month<0) || (Day >31 || Day<0) || (Hour >24 || Hour<0) || (Minute>59 || Minute<0)){
                create.setError("You must insert valid parameters");
                create.requestFocus();
                Toast.makeText(EventsScreen.this, "You must insert valid parameters", Toast.LENGTH_LONG).show();
                return;
            }
            if(EventAdapter.getNum_checked()>=MinNumBooksForEvent){
                selected_books = EventAdapter.getSelectedBooks();
            }
            else{
                create.setError("You must choose at least "+MinNumBooksForEvent +" books");
                create.requestFocus();
                Toast.makeText(EventsScreen.this, "You must choose at least "+MinNumBooksForEvent +" books", Toast.LENGTH_LONG).show();
                return;
            }
            final String uid = managerAuth.getCurrentUser().getUid();
            final Event event = new Event(selected_books, Day, Month, Hour, Minute);

            // assign the created event to the manager and navigate to the menu screen
            managerRef.child(uid).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    Manager manager = snapshot.getValue(Manager.class);
                    if(manager != null){
                        manager.setEvent(event);
                        managerRef.child(uid).setValue(manager);
                        Toast.makeText(EventsScreen.this, "The event is created!", Toast.LENGTH_LONG);
                        startActivity(new Intent(EventsScreen.this, ManagerMenu.class));
                        finish();
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    Toast.makeText(EventsScreen.this, "Something was wrong!", Toast.LENGTH_LONG);
                }
            });

            //assign true to event_exist
            final DatabaseReference eventRef = FirebaseDatabase.getInstance().getReference("ManagerTools").child("event_uid");
            eventRef.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    eventRef.setValue(uid);
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        }
    }
}