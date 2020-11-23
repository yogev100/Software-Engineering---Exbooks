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


public class ForgetPasswordScreen extends AppCompatActivity implements View.OnClickListener {
    EditText email;
    Button sendpass;

    private FirebaseAuth cAuth;
    private DatabaseReference dbRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.forget_password_screen);

        email = (EditText)findViewById(R.id.mail_textview);
        sendpass=(Button)findViewById(R.id.reset_pass_button);
        sendpass.setOnClickListener(this);

        cAuth=FirebaseAuth.getInstance();
        dbRef=FirebaseDatabase.getInstance().getReference().child("Users").child("Clients");

    }

    @Override
    public void onClick(View view) {
        String mail=email.getText().toString();

        if(mail.isEmpty()){
            email.setError("Email address required!");
            email.requestFocus();
            return;
        }
//        if(!valid_mail(mail)){
//            email.setError("Email address is already exist!");
//            email.requestFocus();
//            return;
//        }
        if(!Patterns.EMAIL_ADDRESS.matcher(mail).matches()){
            email.setError("Please provide valid email!");
            email.requestFocus();
            return;
        }

        cAuth.sendPasswordResetEmail(mail).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()){



                    // **********************************************
                    // i need to update the password at the database
                    // **********************************************




                    //send it to firebase
                    //dbRef.child(FirebaseAuth.getInstance().getCurrentUser().getUid());
                    //FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                    //dbRef.child(String.valueOf(this)).setValue(cAuth.getCurrentUser());
                    //dbRef.child(FirebaseAuth.getInstance().getCurrentUser().getUid()).setValue(cAuth.getCurrentUser());
                    Toast.makeText(ForgetPasswordScreen.this,"Reset password via email",Toast.LENGTH_LONG).show();
                    startActivity(new Intent(ForgetPasswordScreen.this, MainActivity.class));
                    finish();
                }
            }
        });
    }
}
