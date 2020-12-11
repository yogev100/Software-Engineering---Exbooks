package com.example.exbooks.Screens;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.example.exbooks.Objects.NotificationCounter;
import com.example.exbooks.R;
import com.example.exbooks.Users.Client;
import com.example.exbooks.Users.Manager;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class ManagerMenu extends AppCompatActivity implements View.OnClickListener {

    Button search,upload,events,profile,logout,newManager,createEvent;
    ConstraintLayout notification;
    FirebaseAuth cAuth;
    NotificationCounter notificationCounter;
    final int minBookSizeForEvent = 2;
    int current_donated;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manger_menu);

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

        logout = (Button)findViewById(R.id.M_logout_button);
        logout.setOnClickListener(this);

        notification=(ConstraintLayout)findViewById(R.id.bell);
        notification.setOnClickListener(this);
        int num=findNumOfNotification();
        System.out.println(num+"out1");
        notificationCounter=new NotificationCounter(findViewById(R.id.bell),num);
        System.out.println(num+"out2");
        notificationCounter.increaseNumber(num);






        cAuth=FirebaseAuth.getInstance();

        CheckDonate();

    }

    private int findNumOfNotification() {
        DatabaseReference mRef=FirebaseDatabase.getInstance().getReference("Users").child("Managers");
        DatabaseReference cRef=FirebaseDatabase.getInstance().getReference("Users").child("Clients");
        String uid=FirebaseAuth.getInstance().getCurrentUser().getUid();
        final int[] num = new int[1];
        mRef.child(uid).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Manager m = snapshot.getValue(Manager.class);
                if(m!=null){
                    num[0] = m.getNotification().size();
                    System.out.println(num[0]);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        cRef.child(uid).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Client c = snapshot.getValue(Client.class);
                if(c!=null){
                    num[0] = c.getNotification().size();
                    System.out.println(num[0]);

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    return num[0];
    }

    private void CheckDonate() {
        String uid = cAuth.getCurrentUser().getUid();
        DatabaseReference managerRef = FirebaseDatabase.getInstance().getReference("Users").child("Managers");
        managerRef.child(uid).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Manager manager = snapshot.getValue(Manager.class);
                if(manager != null){
                    current_donated = manager.getNum_of_books_donated();
                    System.out.println(current_donated);
                    if(current_donated<minBookSizeForEvent){
                        createEvent.setVisibility(View.INVISIBLE);
                    }
                    else{
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
            Intent intent = new Intent(ManagerMenu.this,EventsScreen.class);
            startActivity(intent);
        }else if(view == profile){
            Intent intent = new Intent(ManagerMenu.this,ProfileScreen.class);
            startActivity(intent);
        }else if(view == newManager){
            Intent intent = new Intent(ManagerMenu.this,NewManager.class);
            startActivity(intent);
        }else if(view == notification){
            notificationCounter.increaseNumber(0);
            Intent intent = new Intent(ManagerMenu.this,NotificationScreen.class);
            startActivity(intent);
        }else if(view == createEvent){
            Intent intent = new Intent(ManagerMenu.this,EventsScreen.class);
            startActivity(intent);
        }else if(view == logout){
            cAuth.signOut();
            Intent intent = new Intent(ManagerMenu.this,MainActivity.class);
            startActivity(intent);
            finish();
        }

    }
}