package com.example.exbooks.Screens;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.exbooks.Objects.Notification;
import com.example.exbooks.Objects.NotificationAdapter;
import com.example.exbooks.R;
import com.example.exbooks.Users.Client;
import com.example.exbooks.Users.Manager;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import android.widget.ScrollView;

import java.util.ArrayList;
import java.util.Collections;

public class NotificationScreen extends AppCompatActivity implements View.OnClickListener{
    FirebaseAuth mAuth;
    DatabaseReference Mref;
    DatabaseReference Cref;
    ScrollView sv;

    private static NotificationAdapter adapter;
    ListView listView;
    ArrayList<Notification> notification_model;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification_screen);

        listView=(ListView)findViewById(R.id.list_of_notification);
        notification_model =new ArrayList<>();

        mAuth=FirebaseAuth.getInstance();
        sv=(ScrollView)findViewById(R.id.notification_scrollView);
        Mref= FirebaseDatabase.getInstance().getReference("Users").child("Managers");
        Cref= FirebaseDatabase.getInstance().getReference("Users").child("Clients");

        NotificationUpload();


        
    }

    private void NotificationUpload() {
        Cref.child(mAuth.getCurrentUser().getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Client c = snapshot.getValue(Client.class);
                if(c!=null){
                    for(Notification n:c.getNotification()){
                        notification_model.add(new Notification(n,n.isFirst()));
                    }
                    Collections.reverse(notification_model);
                    adapter=new NotificationAdapter(notification_model,getApplicationContext(),getSupportFragmentManager());
                    listView.setAdapter(adapter);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        Mref.child(mAuth.getCurrentUser().getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Manager m = snapshot.getValue(Manager.class);
                if(m!=null){
                    for(Notification n:m.getNotification()){
                        notification_model.add(new Notification(n,n.isFirst()));
                    }
                    Collections.reverse(notification_model);
                    adapter=new NotificationAdapter(notification_model,getApplicationContext(),getSupportFragmentManager());
                    listView.setAdapter(adapter);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    @Override
    public void onClick(View v) {

    }
}