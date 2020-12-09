package com.example.exbooks.Screens;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.exbooks.Objects.Notification;
import com.example.exbooks.R;
import com.example.exbooks.Users.Manager;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import android.os.Bundle;
import android.widget.ScrollView;
import android.widget.TextView;

public class NotificationScreen extends AppCompatActivity {
    FirebaseAuth mAuth;
    DatabaseReference Mref;
    DatabaseReference Cref;
    ScrollView sv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification_screen);
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
                Manager m = snapshot.getValue(Manager.class);
                if(m!=null){
                    for(Notification n:m.getNotification()){
                        TextView ts=new TextView(getApplicationContext());
                        ts.setText("Someone wants the book: "+n.getBook_name());
                        sv.addView(ts);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        Mref.child(mAuth.getCurrentUser().getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Manager c = snapshot.getValue(Manager.class);
                if(c!=null){
                    for(Notification n:c.getNotification()){
                        TextView ts=new TextView(getApplicationContext());
                        ts.setText("Someone wants the book: "+n.getBook_name());
                        sv.addView(ts);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }
}