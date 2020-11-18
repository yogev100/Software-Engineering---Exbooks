package com.example.exbooks.Screens;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.UserManager;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
//import com.google.firebase.auth.AuthResult;
//import com.google.firebase.auth.FirebaseAuth;
//import com.google.firebase.auth.FirebaseUser;
import com.example.exbooks.Users.Client;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.example.exbooks.R;

public class RegisterScreen extends AppCompatActivity implements View.OnClickListener {
    final String[] manager_emails = {"yogev2468@gmail.com"};
    EditText fullname;
    EditText username;
    EditText password;
    EditText re_password;
    EditText city;
    EditText email;
    EditText phone;
    Button signup;

    private FirebaseAuth cAuth;
    private DatabaseReference dbRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register_screen);

        fullname = (EditText)findViewById(R.id.fullname_textview);
        username = (EditText)findViewById(R.id.username_textview);
        password = (EditText)findViewById(R.id.passwordragister_textview);
        re_password = (EditText)findViewById(R.id.repasswordragister_textview);
        city = (EditText)findViewById(R.id.city_textview);
        email = (EditText)findViewById(R.id.email_textview);
        phone = (EditText)findViewById(R.id.phone_textview);

        signup=(Button)findViewById(R.id.signup_button);
        signup.setOnClickListener(this);

        cAuth=FirebaseAuth.getInstance();
        dbRef=FirebaseDatabase.getInstance().getReference().child("Users").child("Clients");
    }

    @Override
    public void onClick(View view) {
        String flname=fullname.getText().toString();
        String usrname=username.getText().toString();
        String pswrd=password.getText().toString();
        String re_pswrd=re_password.getText().toString();
        String cty=city.getText().toString();
        String mail=email.getText().toString();
        String phn=phone.getText().toString();

        if(flname.isEmpty()){
            fullname.setError("Full name is required!");
            fullname.requestFocus();
            return;
        }
        if(usrname.isEmpty()){
            username.setError("Username is required!");
            username.requestFocus();
            return;
        }
        if(pswrd.isEmpty()){
            password.setError("Password is required!");
            password.requestFocus();
            return;
        }
        if(pswrd.length() < 6){
            password.setError("Min password should be 6 characters!");
            password.requestFocus();
            return;
        }
        if(!pswrd.equals(re_pswrd)){
            re_password.setError("Wrong password verification!");
            re_password.requestFocus();
            return;
        }
        if(mail.isEmpty()){
            email.setError("Email address required!");
            email.requestFocus();
            return;
        }
        if(!valid_mail(mail)){
            email.setError("Email address is already exist!");
            email.requestFocus();
            return;
        }
        if(!Patterns.EMAIL_ADDRESS.matcher(mail).matches()){
            email.setError("Please provide valid email!");
            email.requestFocus();
            return;
        }
        if(cty.isEmpty()){
            city.setError("City is required!");
            city.requestFocus();
            return;
        }
        if(phn.length() != 10) {
            phone.setError("Please provide valid number!");
            phone.requestFocus();
            return;
        }

        cAuth.createUserWithEmailAndPassword(mail,mail).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    Client client = new Client(username.getText().toString(),fullname.getText().toString(),
                            email.getText().toString(),password.getText().toString(),city.getText().toString(),phone.getText().toString());
                    //send it to firebase
                    dbRef.child(FirebaseAuth.getInstance().getCurrentUser().getUid()).setValue(client).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                            if(task.isSuccessful()) {
                                user.sendEmailVerification();
                                Toast.makeText(RegisterScreen.this,"Registration successfully, mail verification sent to your email",Toast.LENGTH_LONG).show();
                                startActivity(new Intent(RegisterScreen.this, MainActivity.class));
                                finish();
                            }else{
                                Toast.makeText(RegisterScreen.this,
                                        "Failed to registered! Try again!",
                                        Toast.LENGTH_LONG).show();
                            }
                        }
                    });
                }
                else{
                    Toast.makeText(RegisterScreen.this,
                            "Failed to register",
                            Toast.LENGTH_LONG).show();
                }
            }
        });

    }

    private boolean valid_mail(String email) {
        return true;
    }
}