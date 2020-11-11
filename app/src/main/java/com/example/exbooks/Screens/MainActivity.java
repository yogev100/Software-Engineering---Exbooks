package com.example.exbooks.Screens;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import android.widget.TextView;
import android.widget.Toast;

import com.example.exbooks.R;

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
        login = (Button) findViewById(R.id.signup_button);

        username = (TextView) findViewById(R.id.fullname_textview);
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
            Toast.makeText(this,"Thanks for Logging in!!",Toast.LENGTH_LONG).show();
            startActivity(intent);
        }
    }
}