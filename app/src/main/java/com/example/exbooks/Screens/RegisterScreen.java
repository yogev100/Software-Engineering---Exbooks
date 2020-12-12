package com.example.exbooks.Screens;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import com.example.exbooks.R;
import com.example.exbooks.Users.Client;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

/**
 * This class used to sign up to the Application. this is the register screen.
 */
public class RegisterScreen extends AppCompatActivity implements View.OnClickListener {
    EditText fullname;
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

    /*
    function that execute when the user click on the register button,
    check all the inserted data and register the new user.
     */
    @Override
    public void onClick(View view) {
        String flname=fullname.getText().toString();
        String pswrd=password.getText().toString();
        String re_pswrd=re_password.getText().toString();
        String cty=city.getText().toString();
        String mail=email.getText().toString();
        String phn=phone.getText().toString();

        // validation checks
        if(flname.isEmpty()){
            fullname.setError("Full name is required!");
            fullname.requestFocus();
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

        //here we create user with email and password
        cAuth.createUserWithEmailAndPassword(mail,pswrd).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    Client client = new Client(fullname.getText().toString(),
                            email.getText().toString(),password.getText().toString(),city.getText().toString(),phone.getText().toString());

                    //send it to firebase
                    dbRef.child(FirebaseAuth.getInstance().getCurrentUser().getUid()).setValue(client).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                            if(task.isSuccessful()) {
                                // Email verification
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
}