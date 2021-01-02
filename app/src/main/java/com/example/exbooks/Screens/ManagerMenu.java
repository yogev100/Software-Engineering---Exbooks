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
import androidx.constraintlayout.widget.ConstraintSet;

import com.example.exbooks.Objects.Notification;
import com.example.exbooks.Objects.NotificationCounter;
import com.example.exbooks.R;
import com.example.exbooks.Users.Manager;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * This class represents the Manager menu,
 * the manager can find a book, upload a book, make an events, change his details from the profile,
 * make a new manager account etc..
 */

public class ManagerMenu extends AppCompatActivity implements View.OnClickListener {

    Button search,upload,events,profile,logout,newManager,createEvent,editClients;
    ConstraintLayout notification;
    FirebaseAuth cAuth;
    NotificationCounter notificationCounter;
    final int[] numOfNotifications = new int[1];
    final int minBookSizeForEvent = 2;
    int current_donated;
    private boolean event_time_in = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manger_menu);
        events = (Button)findViewById(R.id.M_events_button);
        events.setOnClickListener(this);
        assignEvent();
        findNumOfNotification();

        cAuth=FirebaseAuth.getInstance();

        current_donated = 0;

        search = (Button)findViewById(R.id.M_signup_button);
        search.setOnClickListener(this);

        upload = (Button)findViewById(R.id.M_upload_button);
        upload.setOnClickListener(this);



        profile = (Button)findViewById(R.id.M_profile_button);
        profile.setOnClickListener(this);

        newManager = (Button)findViewById(R.id.M_new_manager);
        newManager.setOnClickListener(this);

        createEvent = (Button)findViewById(R.id.M_newevents_Button);
        createEvent.setOnClickListener(this);
        CheckDonate();

        editClients = (Button)findViewById(R.id.edit_clients);
        editClients.setOnClickListener(this);

        logout = (Button)findViewById(R.id.M_logout_button);
        logout.setOnClickListener(this);

        notification=(ConstraintLayout)findViewById(R.id.bell);
        notification.setOnClickListener(this);

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
                    ConstraintLayout constraintLayout = findViewById(R.id.ManagerMenu);
                    ConstraintSet constraintSet = new ConstraintSet();
                    constraintSet.clone(constraintLayout);
                    constraintSet.connect(R.id.M_profile_button, ConstraintSet.TOP,R.id.M_upload_button,ConstraintSet.BOTTOM,16);
                    constraintSet.applyTo(constraintLayout);
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



    // Method that update the number of notifications
    private void findNumOfNotification(){
        DatabaseReference mRef=FirebaseDatabase.getInstance().getReference("Users").child("Managers");
        String uid=FirebaseAuth.getInstance().getCurrentUser().getUid();
        mRef.child(uid).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Manager m = snapshot.getValue(Manager.class);
                if(m!=null){
                    int size=0;
                    for(Notification n:m.getNotification()){
                        if(n.isNewNotification()){
                            size++;
                        }
                    }
                    numOfNotifications[0]=size;
                    notificationCounter=new NotificationCounter(findViewById(R.id.bell), size);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    // Method that checks if there enough donated books for event. and make the button visible/invisible in accordance
    private void CheckDonate() {
        String uid = cAuth.getCurrentUser().getUid();
        DatabaseReference numRef = FirebaseDatabase.getInstance().getReference("ManagerTools");
        numRef.child("num_of_books_donated").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Integer i = snapshot.getValue(Integer.class);
                if(i!=null){
                    if(i<minBookSizeForEvent){                      // there not enough books for event
                        createEvent.setVisibility(View.INVISIBLE);
                    }else{                                          // enough books. can make event.
                        createEvent.setVisibility(View.VISIBLE);
                    }
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
            Intent intent = new Intent(ManagerMenu.this,SearchScreen.class);
            startActivity(intent);
        }else if(view == upload) {
            Intent intent = new Intent(ManagerMenu.this, UploadScreen.class);
            startActivity(intent);
        }else if(view == events){
              Intent intent = new Intent(ManagerMenu.this,BookEventScreen.class);
              startActivity(intent);
        }else if(view == profile){
            Intent intent = new Intent(ManagerMenu.this,ProfileScreen.class);
            startActivity(intent);
        }else if(view == newManager){
            Intent intent = new Intent(ManagerMenu.this,NewManager.class);
            startActivity(intent);
        }else if(view == notification){
            Intent intent = new Intent(ManagerMenu.this,NotificationScreen.class);
            startActivity(intent);
        }else if(view == createEvent){
            Intent intent = new Intent(ManagerMenu.this,EventsScreen.class);
            startActivity(intent);
        }else if(view == editClients){
            Intent intent = new Intent(ManagerMenu.this,EditClientsScreen.class);
            startActivity(intent);
        }else if(view == logout){
            cAuth.signOut();
            Intent intent = new Intent(ManagerMenu.this,MainActivity.class);
            startActivity(intent);
            finish();
        }
    }
}