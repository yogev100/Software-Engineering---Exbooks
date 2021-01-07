package com.example.exbooks.Screens;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.example.exbooks.Objects.Notification;
import com.example.exbooks.Objects.NotificationCounter;
import com.example.exbooks.R;
import com.example.exbooks.Users.Client;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * This class represents the Client menu,
 * the client can find a book, upload a book, check for events, change his details from the profile, etc..
 */

public class CustomerMenu extends AppCompatActivity implements View.OnClickListener {

    Button search,upload,events,profile,logout;
    ConstraintLayout notification;
    FirebaseAuth cAuth;
    final int[] numOfNotifications = new int[1];
    NotificationCounter notificationCounter;
    private boolean event_time_in = false;
    String event_uid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_menu);
        assignEvent();
        findNumOfNotification();

        search = (Button)findViewById(R.id.signup_button);
        search.setOnClickListener(this);

        upload = (Button)findViewById(R.id.upload_button);
        upload.setOnClickListener(this);

        events = (Button)findViewById(R.id.events_button);
        events.setOnClickListener(this);

        profile = (Button)findViewById(R.id.profile_button);
        profile.setOnClickListener(this);

        logout = (Button)findViewById(R.id.logout_button);
        logout.setOnClickListener(this);


        notification=(ConstraintLayout)findViewById(R.id.bell);
        notification.setOnClickListener(this);

        cAuth=FirebaseAuth.getInstance();

    }

    private void assignEvent() {

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
                    LocalDateTime currTimeIsrael =LocalDateTime.of(currTimeMinus2.getYear(), currTimeMinus2.getMonth(), currTimeMinus2.getDayOfMonth(), currTimeMinus2.getHour()+2, currTimeMinus2.getMinute(), 0);
                    LocalDateTime dateFinishEvent = LocalDateTime.of(dateTimeEvent.getYear(), dateTimeEvent.getMonth(), dateTimeEvent.getDayOfMonth(), dateTimeEvent.getHour()+2, dateTimeEvent.getMinute(), 0);

                    if(currTimeIsrael.isAfter(dateTimeEvent) && currTimeIsrael.isBefore(dateFinishEvent)){
                        event_time_in = true;
                    }
                }

                if(!event_time_in){
                    events.setVisibility(View.INVISIBLE);

                }
                else{
                    events.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }


    @Override
    public void onClick(View view) {
        if(view == search){
            Intent intent = new Intent(CustomerMenu.this,SearchScreen.class);
            startActivity(intent);
        }else if(view == upload) {
            Intent intent = new Intent(CustomerMenu.this, UploadScreen.class);
            startActivity(intent);
        }else if(view == events){
            Intent intent = new Intent(CustomerMenu.this,BookEventScreen.class);
            startActivity(intent);
        }else if(view == profile){
            Intent intent = new Intent(CustomerMenu.this,ProfileScreen.class);
            startActivity(intent);
        }else if(view == notification){
            Intent intent = new Intent(CustomerMenu.this,NotificationScreen.class);
            intent.putExtra("isClient",true);
            startActivity(intent);
        }else if(view == logout){
            cAuth.signOut();
            Intent intent = new Intent(CustomerMenu.this,MainActivity.class);
            startActivity(intent);
            finish();
        }
    }

    // This method find the number of the notification and update it on the bell
    private void findNumOfNotification() {
        DatabaseReference cRef=FirebaseDatabase.getInstance().getReference("Users").child("Clients");
        String uid=FirebaseAuth.getInstance().getCurrentUser().getUid();

        cRef.child(uid).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Client c = snapshot.getValue(Client.class);
                if(c!=null){
                    int size=0;
                    for(Notification n:c.getNotification()){
                        if(n.isNewNotification()){
                            size++;
                        }
                    }
                    numOfNotifications[0]=size;
                    notificationCounter=new NotificationCounter(findViewById(R.id.bell),size);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}