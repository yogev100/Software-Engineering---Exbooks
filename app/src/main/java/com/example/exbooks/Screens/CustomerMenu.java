package com.example.exbooks.Screens;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.example.exbooks.Objects.NotificationCounter;
import com.example.exbooks.R;
import com.example.exbooks.Users.Client;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class CustomerMenu extends AppCompatActivity implements View.OnClickListener {

    Button search,upload,events,profile,logout;
    ConstraintLayout notification;
    FirebaseAuth cAuth;
//    ImageButton notification;
    NotificationCounter notificationCounter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_menu);

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



//        notification=(ImageButton)findViewById(R.id.notification_button);
        notification=(ConstraintLayout)findViewById(R.id.bell);
        notification.setOnClickListener(this);
        int num=findNumOfNotification();
        System.out.println(num+"out1");
        notificationCounter=new NotificationCounter(findViewById(R.id.bell),num);
        System.out.println(num+"out2");
        notificationCounter.increaseNumber(num);
        cAuth=FirebaseAuth.getInstance();


    }

    @Override
    public void onClick(View view) {
        if(view == search){
            Intent intent = new Intent(CustomerMenu.this,SearchScreen.class);
            startActivity(intent);
        }else if(view == upload) {
            Intent intent = new Intent(CustomerMenu.this, UploadScreen.class);
            startActivity(intent);
//        }else if(view == events){
//            Intent intent = new Intent(CustomerMenu.this,EventsScreen.class);
//            startActivity(intent);
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

    private int findNumOfNotification() {
//        DatabaseReference mRef= FirebaseDatabase.getInstance().getReference("Users").child("Managers");
        DatabaseReference cRef=FirebaseDatabase.getInstance().getReference("Users").child("Clients");
        String uid=FirebaseAuth.getInstance().getCurrentUser().getUid();
        final int[] num = new int[1];
//        mRef.child(uid).addListenerForSingleValueEvent(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot snapshot) {
//                Manager m = snapshot.getValue(Manager.class);
//                if(m!=null){
//                    num[0] = m.getNotification().size();
//                    System.out.println(num[0]);
//                }
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError error) {
//
//            }
//        });
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
}