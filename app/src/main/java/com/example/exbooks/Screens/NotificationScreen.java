package com.example.exbooks.Screens;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.exbooks.Objects.Notification;
import com.example.exbooks.Adapters.NotificationAdapter;
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
import android.widget.ListView;
import android.widget.ScrollView;

import java.util.ArrayList;
import java.util.Collections;

/**
 * This class used to manage the notification, update and show them.
 */
public class NotificationScreen extends AppCompatActivity{
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

    // This method go all over the notification, add to the list and show them by the adapter.
    private void NotificationUpload() {
        Cref.child(mAuth.getCurrentUser().getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Client c = snapshot.getValue(Client.class);
                if(c!=null){
                    ArrayList <Notification> empty_new = c.getNotification();
                    int i=0;
                    for(Notification n:c.getNotification()){                                // go all over the notification
                        empty_new.get(i).setNewNotification(false);
                        notification_model.add(new Notification(n,n.isFirst(),false));            // add the notification to the list
                        i++;
                    }
                    c.setNotification(empty_new);
                    Cref.child(c.getUid()).setValue(c);
                    Collections.reverse(notification_model);                                // make it reverse, and show by the adapter.
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
                    ArrayList <Notification> empty_new = m.getNotification();
                    int i=0;
                    for(Notification n:m.getNotification()){                                // go all over the notifications
                        empty_new.get(i).setNewNotification(false);
                        notification_model.add(new Notification(n,n.isFirst(),false));            // add the notification to the list
                        i++;
                    }
                    m.setNotification(empty_new);
                    Cref.child(mAuth.getCurrentUser().getUid()).setValue(m);
                    Collections.reverse(notification_model);                                // make it reverse, and show by the adapter.
                    adapter=new NotificationAdapter(notification_model,getApplicationContext(),getSupportFragmentManager());
                    listView.setAdapter(adapter);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }
}