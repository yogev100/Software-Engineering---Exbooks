package com.example.exbooks.Screens;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.exbooks.R;
import com.example.exbooks.Users.Client;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class NewManager extends AppCompatActivity implements View.OnClickListener{
    TextView mail;
    Button menu;
    Button createMangaer;

    FirebaseAuth cAuth;
    DatabaseReference dbRef;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_manager);

        mail=(TextView)findViewById(R.id.new_manager_mail_textview);

        menu=(Button)findViewById(R.id.goto_menu_Button);
        menu.setOnClickListener(this);

        createMangaer=(Button)findViewById(R.id.create_new_manager_Button);
        createMangaer.setOnClickListener(this);

        dbRef= FirebaseDatabase.getInstance().getReference("Users").child("Clients");
        cAuth=FirebaseAuth.getInstance();
    }

    @Override
    public void onClick(View view) {
        String email = mail.getText().toString().trim();

        if(view==createMangaer){
            PromoteClientToManager(email);
        }
        else if(view == menu){
            startActivity(new Intent(this, ManagerMenu.class));
        }
    }

    // Method that promote a Client to be Manager.
    private void PromoteClientToManager(final String email){
//        System.out.println("In MAAGER PROMOTWE");
        dbRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String newManagerId;
                for(DataSnapshot c:snapshot.getChildren()){
                    Client cl=c.getValue(Client.class);
                    // check if the client user is not null, and if this is his email..
                    if(cl!= null && cl.getEmail().equals(email)){
                        newManagerId=c.getKey(); //uid client
//                        System.out.println(newManagerId);
                        DatabaseReference managers=FirebaseDatabase.getInstance().getReference("Users").child("Managers");

                        // update the new manager at the Manager tree
                        managers.child(newManagerId).setValue(cl);
                        // and remove from the Client tree.
                        dbRef.child(newManagerId).removeValue();

                        Toast.makeText(NewManager.this, email+ " promoted to manager", Toast.LENGTH_LONG).show();
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}