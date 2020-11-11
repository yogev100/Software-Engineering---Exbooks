package com.example.exbooks.Screens;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.exbooks.R;

public class RegisterScreen extends AppCompatActivity implements View.OnClickListener {
    final String[] manager_emails = {"yogev2468@gmail.com"};
    TextView fullname;
    TextView username;
    TextView password;
    TextView re_password;
    TextView city;
    TextView email;
    TextView phone;

    Button signup;

    //private FirebaseAuth mAuth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register_screen);

        fullname = (TextView)findViewById(R.id.fullname_textview);
        username = (TextView)findViewById(R.id.username_textview);
        password = (TextView)findViewById(R.id.passwordragister_textview);
        re_password = (TextView)findViewById(R.id.repasswordragister_textview);
        city = (TextView)findViewById(R.id.city_textview);
        email = (TextView)findViewById(R.id.email_textview);
        phone = (TextView)findViewById(R.id.phone_textview);

        signup=(Button)findViewById(R.id.signup_button);
        signup.setOnClickListener(this);

    }

    @Override
    public void onClick(View view) {
        if(username.equals("")){
            username.setError("Username is required!");
            username.requestFocus();
            return;
        }
        if(fullname.equals("")){
            fullname.setError("Full name is required!");
            fullname.requestFocus();
            return;
        }
        if(email.equals("")){
            email.setError("Email address required!");
            email.requestFocus();
            return;
        }
        if(!Patterns.EMAIL_ADDRESS.matcher(email.getText().toString()).matches()){
            email.setError("Please provide valid email!");
            email.requestFocus();
            return;
        }
        if(password.equals("")){
            password.setError("Password is required!");
            password.requestFocus();
            return;
        }
        if(password.length() < 6){
            password.setError("Min password should be 6 characters!");
            password.requestFocus();
            return;
        }
        if(password == re_password){
            re_password.setError("Wrong password verification!");
            re_password.requestFocus();
            return;
        }
        if(phone.length() != 10){
            phone.setError("Please provide valid number!");
            phone.requestFocus();
            return;
        }
        if(city.equals("")){
            city.setError("City is required!");
            city.requestFocus();
            return;
        }

    }
}