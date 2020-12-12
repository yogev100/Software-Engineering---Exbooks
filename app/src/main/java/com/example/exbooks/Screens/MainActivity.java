package com.example.exbooks.Screens;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.exbooks.R;
import com.example.exbooks.Users.Client;
import com.example.exbooks.Users.Manager;
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

/**
 * This class is the entry screen of the App.
 * You can to sign up, sign in and reset your password.
 */
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
        forgetpassword.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        if(v == register){
            Intent intent = new Intent(MainActivity.this,RegisterScreen.class);
            startActivity(intent);
        }
        else if(v == login){
            UserLogin();
        }
        else if(v == forgetpassword){
            Intent intent = new Intent(MainActivity.this, ForgetPasswordScreen.class);
            startActivity(intent);
        }
    }

    /*
    function that check validation of the mail and the password of the user (include verify email after registration)
    and if its ok - navigate the user to the menu screen, else - show an error
     */
    private void UserLogin() {
        String email = mail.getText().toString().trim();
        final String paswrd = password.getText().toString().trim();

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

        // here execute the mail and password verification
        cAuth.signInWithEmailAndPassword(email,paswrd).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){

                    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                    DatabaseReference ClientRoot = FirebaseDatabase.getInstance().getReference("Users").child("Clients");
                    DatabaseReference ManagerRoot = FirebaseDatabase.getInstance().getReference("Users").child("Managers");
                    //Redirect to user profile:
                    if(user.isEmailVerified()) {
                        EnterToMenu(ClientRoot, ManagerRoot,user,paswrd);
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

    /*
    function that gets 2 references (to client root and manager root) and navigate the user to the correct menu screen
     */
    private void EnterToMenu(final DatabaseReference ClientRoot,final DatabaseReference ManagerRoot, final FirebaseUser user, final String pswrd) {
        ClientRoot.child(user.getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Client clientProfile = snapshot.getValue(Client.class);
                if(clientProfile != null){
                    // update password if its changed
                    if(!pswrd.equals(clientProfile.getPassword())) {
                        clientProfile.setPassword(pswrd);
                        ClientRoot.child(user.getUid()).setValue(clientProfile);
                    }
                    startActivity(new Intent(MainActivity.this, CustomerMenu.class));
                    finish();
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
                Manager managerProfile = snapshot.getValue(Manager.class);
                if(managerProfile != null){
                    // update password if its changed
                    if(!pswrd.equals(managerProfile.getPassword())) {
                        managerProfile.setPassword(pswrd);
                        ManagerRoot.child(user.getUid()).setValue(managerProfile);
                    }
                    startActivity(new Intent(MainActivity.this, ManagerMenu.class));
                    finish();
                }

            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(MainActivity.this,"Something was wrong!",Toast.LENGTH_LONG);
            }
        });
    }
}