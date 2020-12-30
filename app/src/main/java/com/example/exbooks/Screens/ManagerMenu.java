package com.example.exbooks.Screens;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.ConstraintSet;

import com.example.exbooks.Objects.NotificationCounter;
import com.example.exbooks.R;
import com.example.exbooks.Users.Manager;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

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
    private boolean event_exist = false;
    String event_uid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manger_menu);
        assignEvent();
        findNumOfNotification();

        cAuth=FirebaseAuth.getInstance();

        current_donated = 0;

        search = (Button)findViewById(R.id.M_signup_button);
        search.setOnClickListener(this);

        upload = (Button)findViewById(R.id.M_upload_button);
        upload.setOnClickListener(this);

        events = (Button)findViewById(R.id.M_events_button);
        events.setOnClickListener(this);

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
        DatabaseReference managerRef = FirebaseDatabase.getInstance().getReference("ManagerTools");
        managerRef.child("event_uid").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                    event_uid = snapshot.getValue(String.class);
                    System.out.println("uid:"+event_uid);
                    if(event_uid != null && !event_uid.equals("")){
                        System.out.println("exist ????????????");
                        event_exist = true;
                    }
                if(!event_exist){
                    System.out.println("no existttttttttttttt");
                    events.setVisibility(View.INVISIBLE);
                    ConstraintLayout constraintLayout = findViewById(R.id.ManagerMenu);
                    ConstraintSet constraintSet = new ConstraintSet();
                    constraintSet.clone(constraintLayout);
                    constraintSet.connect(R.id.M_profile_button, ConstraintSet.TOP,R.id.M_upload_button,ConstraintSet.BOTTOM,16);
                    constraintSet.applyTo(constraintLayout);
                }
                else{
                    System.out.println("daskdmakdmak holutlultl");
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
                    numOfNotifications[0] = m.getNotification().size();
                    notificationCounter=new NotificationCounter(findViewById(R.id.bell), numOfNotifications[0]);
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