package com.example.exbooks.Screens;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

import androidx.appcompat.app.AppCompatActivity;

import com.example.exbooks.R;
import com.google.firebase.auth.FirebaseAuth;

public class CustomerMenu extends AppCompatActivity implements View.OnClickListener {

    Button search,upload,events,profile,logout;
    FirebaseAuth cAuth;
    ImageButton notification;
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

        notification=(ImageButton)findViewById(R.id.notification_button);
        notification.setOnClickListener(this);

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
}