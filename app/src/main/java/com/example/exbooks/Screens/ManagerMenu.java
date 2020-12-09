package com.example.exbooks.Screens;

import androidx.appcompat.app.AppCompatActivity;
import com.example.exbooks.R;
import com.google.firebase.auth.FirebaseAuth;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

public class ManagerMenu extends AppCompatActivity implements View.OnClickListener {

    Button search,upload,events,profile,logout,newManager,createEvent;
    ImageButton notification;
    FirebaseAuth cAuth;
    ImageButton settings;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manger_menu);

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

        notification=(ImageButton)findViewById(R.id.M_notification_button);
        notification.setOnClickListener(this);


        cAuth=FirebaseAuth.getInstance();

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
            Intent intent = new Intent(ManagerMenu.this,NotificationScreen.class);
            startActivity(intent);
        }else if(view == createEvent){//Not Done
//            Intent intent = new Intent(MangerMenu.this,ProfileScreen.class);
//            startActivity(intent);
        }else if(view == logout){
            cAuth.signOut();
            Intent intent = new Intent(ManagerMenu.this,MainActivity.class);
            startActivity(intent);
            finish();
        }
    }
}