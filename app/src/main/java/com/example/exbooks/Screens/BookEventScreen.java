package com.example.exbooks.Screens;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
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

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

public class BookEventScreen extends AppCompatActivity implements View.OnClickListener{

    ArrayList<Book> bookModels;
    ListView listView;
    private static BookEventAdapter adapter;
    Button menu_btn;
    TextView timer_text;
    private CountDownTimer countDownTimer;
    private boolean isRunning = true;
    private boolean is_client;
    private long timeLeft;
    private int hours, minutes, seconds;
    private final int eventDurationInMill = 3600000 * 2;
    private String eventUID;
    String uid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.book_event_screen);
        getEventTime();
        isClient();
        menu_btn = (Button)findViewById(R.id.back_from_event);
        menu_btn.setOnClickListener(this);
        timer_text = (TextView)findViewById(R.id.timer);
        listView=(ListView)findViewById(R.id.event_list);

        // set the adapter to the list for show all of them in custom layout
        bookModels = new ArrayList<>();
        getEventBooks();

    }

    private void getEventTime() {
        DatabaseReference eventTimeRef = FirebaseDatabase.getInstance().getReference("ManagerTools").child("event_date");
        eventTimeRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String date_event = snapshot.getValue(String.class);
                if(date_event!=null){
                    DateTimeFormatter formatter1 = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
                    LocalDateTime dateTimeEvent = LocalDateTime.parse(date_event , formatter1);
                    LocalDateTime currTimeMinus2 = LocalDateTime.now();
                    LocalDateTime currTimeIsrael =LocalDateTime.of(currTimeMinus2.getYear(), currTimeMinus2.getMonth(), currTimeMinus2.getDayOfMonth(), currTimeMinus2.getHour()+2, currTimeMinus2.getMinute(), currTimeMinus2.getSecond());
//                    LocalDateTime dateFinishEvent = LocalDateTime.of(dateTimeEvent.getYear(), dateTimeEvent.getMonth(), dateTimeEvent.getDayOfMonth(), dateTimeEvent.getHour()+2, dateTimeEvent.getMinute(), 0);
                    hours = (currTimeIsrael.getHour() - dateTimeEvent.getHour() + 24) % 24;
                    System.out.println("hours:"+hours);
                    minutes = (currTimeIsrael.getMinute() - dateTimeEvent.getMinute() +60) % 60;
                    System.out.println("minutes:"+minutes);
                    if((hours == 1 || hours ==2) && currTimeIsrael.getMinute() < dateTimeEvent.getMinute()){
                        hours--;
                    }
                    seconds = (currTimeIsrael.getSecond() - dateTimeEvent.getSecond() +60) % 60;
                    System.out.println("seconds:"+seconds);
                    long timePassed = hours* 3600000 + minutes * 60000 + seconds * 1000;
                    timeLeft = eventDurationInMill - timePassed;
                    System.out.println("timeLeft:"+timeLeft);
                    startStop();

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void startStop() {
        startTimer();
    }

    public void startTimer(){
        countDownTimer = new CountDownTimer(timeLeft, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                timeLeft = millisUntilFinished;
                updateTimer();
            }

            @Override
            public void onFinish() {
                removeEventFromFireBase();

                Toast.makeText(BookEventScreen.this, "The event is over!", Toast.LENGTH_LONG).show();
                if(is_client){
                    startActivity(new Intent(BookEventScreen.this, CustomerMenu.class));
                    finish();
                }
                else{
                    startActivity(new Intent(BookEventScreen.this, ManagerMenu.class));
                    finish();
                }
            }

            private void removeEventFromFireBase() {
                final DatabaseReference eventDateRef = FirebaseDatabase.getInstance().getReference("ManagerTools").child("event_date");
                eventDateRef.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        eventDateRef.removeValue();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

                final DatabaseReference eventUIDRef = FirebaseDatabase.getInstance().getReference("ManagerTools").child("event_uid");
                eventUIDRef.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        eventUID = snapshot.getValue(String.class);
                        eventUIDRef.removeValue();
                        final DatabaseReference managerEventRef = FirebaseDatabase.getInstance().getReference("Users").child("Managers").child(eventUID).child("event");
                        managerEventRef.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                managerEventRef.removeValue();
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
        }.start();
    }

    public void updateTimer(){
        int min = (int) timeLeft / 60000;
        int sec = (int) timeLeft % 60000 / 1000;

        String timeLeftText;
        timeLeftText = "" + min;
        timeLeftText += ":";
        if(sec<10) timeLeftText +="0";
        timeLeftText += sec;
        timer_text.setText(timeLeftText);
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
