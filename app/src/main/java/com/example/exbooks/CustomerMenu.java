package com.example.exbooks;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

public class CustomerMenu extends AppCompatActivity implements View.OnClickListener {

    Button search,upload,events,profile,logout;
    ImageButton settings;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_menu);
        search = (Button)findViewById(R.id.search_button);
        search.setOnClickListener(this);

        upload = (Button)findViewById(R.id.upload_button);
        upload.setOnClickListener(this);

        events = (Button)findViewById(R.id.events_button);
        events.setOnClickListener(this);

        profile = (Button)findViewById(R.id.profile_button);
        profile.setOnClickListener(this);

        logout = (Button)findViewById(R.id.logout_button);
        logout.setOnClickListener(this);

    }

    @Override
    public void onClick(View view) {
        if(view == search){
            Intent intent = new Intent(CustomerMenu.this,SearchScreen.class);
            startActivity(intent);
        }
    }
}