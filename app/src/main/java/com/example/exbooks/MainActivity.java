package com.example.exbooks;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    TextView register;
    Button login;
    TextView username;
    TextView password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        register = (TextView) findViewById(R.id.register_button);
        login = (Button) findViewById(R.id.search_button);

        username = (TextView) findViewById(R.id.username_textview);
        password = (TextView) findViewById(R.id.password_textview);

        login.setOnClickListener(this);
        register.setOnClickListener(MainActivity.this);

    }

    @Override
    public void onClick(View v) {
        if(v == register){
            Intent intent = new Intent(MainActivity.this,RegisterScreen.class);
            startActivity(intent);
        }
        else if(v == login){
            Intent intent = new Intent(MainActivity.this,CustomerMenu.class);
            intent.putExtra("username",username.getText().toString());
            intent.putExtra("password",password.getText().toString());
            Toast.makeText(this,"Thank for Logging in!!",Toast.LENGTH_LONG).show();
            startActivity(intent);
        }
    }
}