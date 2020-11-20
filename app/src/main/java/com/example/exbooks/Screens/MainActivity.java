package com.example.exbooks.Screens;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;

import android.widget.TextView;
import android.widget.Toast;

import com.example.exbooks.R;
import com.example.exbooks.Users.Client;
import com.example.exbooks.Users.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    TextView register;
    Button login;
    TextView mail;
    TextView password;
    TextView forgetpassword;
    private FirebaseAuth cAuth;
    private DatabaseReference dbRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        register = (TextView) findViewById(R.id.register_button);
        login = (Button) findViewById(R.id.signup_button);
        forgetpassword = (TextView) findViewById(R.id.forgetpassword_textview);


        mail = (TextView) findViewById(R.id.mail_textview);
        password = (TextView) findViewById(R.id.password_textview);

        cAuth = FirebaseAuth.getInstance();

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
            ClientLogin();
        }
        else if(v == forgetpassword){

        }
    }

    private void ClientLogin() {
        String email = mail.getText().toString().trim();
        String paswrd = password.getText().toString().trim();

        if(email.isEmpty()){
            mail.setError("Email is required!");
            mail.requestFocus();
            return;
        }
        if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            mail.setError("Please provide valid email!");
            mail.requestFocus();
            return;
        }
        if(paswrd.isEmpty()){
            password.setError("Password is required!");
            password.requestFocus();
            return;
        }
        if(paswrd.length() < 6){
            password.setError("Min password should be 6 characters!");
            password.requestFocus();
            return;
        }


        cAuth.signInWithEmailAndPassword(email,paswrd).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){

                    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                    DatabaseReference ClientRoot = FirebaseDatabase.getInstance().getReference("Users").child("Clients");
                    DatabaseReference ManagerRoot = FirebaseDatabase.getInstance().getReference("Users").child("Managers");

                    //Redirect to user profile:
                    if(user.isEmailVerified()) {
                        EnterToMenu(ClientRoot, ManagerRoot,user);
                    }
                    else {
                        user.sendEmailVerification();
                        Toast.makeText(MainActivity.this, "you must verify your account via email", Toast.LENGTH_LONG).show();

                    }
                }else{

                    Toast.makeText(MainActivity.this,"Email or Password was incorrect!",Toast.LENGTH_LONG).show();

                }
            }
        });
    }

    private void EnterToMenu(DatabaseReference ClientRoot, DatabaseReference ManagerRoot, final FirebaseUser user) {
        ClientRoot.child(user.getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Client clientProfile = snapshot.getValue(Client.class);
                if(clientProfile != null){
                    startActivity(new Intent(MainActivity.this, CustomerMenu.class));
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(MainActivity.this,"Something was wrong!",Toast.LENGTH_LONG);
            }
        });

        ManagerRoot.child(user.getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                User userProfile = snapshot.getValue(User.class);
                if(userProfile != null){
                    startActivity(new Intent(MainActivity.this, CustomerMenu.class));
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(MainActivity.this,"Something was wrong!",Toast.LENGTH_LONG);
            }
        });
    }
}