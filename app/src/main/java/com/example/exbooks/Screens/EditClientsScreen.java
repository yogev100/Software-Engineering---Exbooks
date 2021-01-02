package com.example.exbooks.Screens;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.exbooks.Adapters.EditProfilesAdapter;
import com.example.exbooks.R;
import com.example.exbooks.Users.Client;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class EditClientsScreen extends AppCompatActivity implements View.OnClickListener{


    Button back_menu_btn;
    ArrayList<Client> profilesModels;
    ListView listView;
    private static EditProfilesAdapter adapter;
    DatabaseReference cRef= FirebaseDatabase.getInstance().getReference("Users").child("Clients");


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.edit_clients_screen);

        // "go back to menu" button
        back_menu_btn = (Button)findViewById(R.id.backToMenuFromEditScreen);
        back_menu_btn.setOnClickListener(this);

        listView=(ListView)findViewById(R.id.edit_clients_list);

        // list of clients
        profilesModels=new ArrayList<>();
        myClientsListInit();
    }


    // Method that init the clients list.
    public void myClientsListInit() {
        cRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                // go all over the clients, and check- if its not null
                // and add the clients to the list.
                    for (DataSnapshot client : snapshot.getChildren()) {
                        Client clientProfile = client.getValue(Client.class);
                        if(clientProfile != null){
                            profilesModels.add(clientProfile);
                        }
                    }
                // turn the adapter on and set its on the listview.
                adapter = new EditProfilesAdapter(profilesModels, getApplicationContext());
                listView.setAdapter(adapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }

    @Override
    public void onClick(View v) {
        // clicked on the "go back to menu" button.
        if (v == back_menu_btn){
            startActivity(new Intent(EditClientsScreen.this, ManagerMenu.class));
            finish();
        }
    }


}
