package com.example.exbooks.Screens;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.exbooks.Objects.Book;
import com.example.exbooks.Objects.EventAdapter;
import com.example.exbooks.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class EventsScreen extends AppCompatActivity implements View.OnClickListener{

    ArrayList<Book> bookModels;
    ListView listView;
    EditText month,day,hour,minute;
    private static EventAdapter adapter;
    DatabaseReference books_ref;
    Button create;
    final int MinNumBooksForEvent = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.event_screen);
        books_ref = FirebaseDatabase.getInstance().getReference("Books");

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
        if(v==create){
            String monthh = month.getText().toString();
            String dayy = day.getText().toString();
            String hourr = hour.getText().toString();
            String minutee = minute.getText().toString();

//            System.out.println("in create click");
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
                ArrayList<Book> selected_books = EventAdapter.getSelectedBooks();

            }
            else{
                create.setError("You must choose at least "+MinNumBooksForEvent +" books");
                create.requestFocus();
                Toast.makeText(EventsScreen.this, "You must choose at least "+MinNumBooksForEvent +" books", Toast.LENGTH_LONG).show();
                return;
            }


        }
    }
}