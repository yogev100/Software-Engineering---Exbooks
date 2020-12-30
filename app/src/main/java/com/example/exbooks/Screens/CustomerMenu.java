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
import com.example.exbooks.Users.Client;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

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
    private boolean event_exist = false;
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
                    numOfNotifications[0] = c.getNotification().size();
                    notificationCounter=new NotificationCounter(findViewById(R.id.bell),numOfNotifications[0]);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}