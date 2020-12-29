package com.example.exbooks.Adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.example.exbooks.Objects.Book;
import com.example.exbooks.R;
import com.example.exbooks.Screens.EditClientsProfileScreen;
import com.example.exbooks.Screens.EditClientsScreen;
import com.example.exbooks.Screens.ProfileScreen;
import com.example.exbooks.Screens.UploadScreen;
import com.example.exbooks.Users.Client;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class EditProfilesAdapter extends ArrayAdapter<Client> implements View.OnClickListener{

    private ArrayList<Client> dataSet;
    Context mContext;
    DatabaseReference cRef= FirebaseDatabase.getInstance().getReference("Users").child("Clients");
    FirebaseAuth cAuth = FirebaseAuth.getInstance();

    // View lookup cache
    private static class ViewHolder {
        TextView Name;
        TextView Email;
        Button chooseButton;
        String uid;
    }

    public EditProfilesAdapter(ArrayList<Client> data, Context context) {
        super(context, R.layout.edit_clients_profiles_component, data);
        this.dataSet = data;
        this.mContext=context;
    }


    // onClick method, checks if the user clicks on the right position.
    @Override
    public void onClick(View v) {
        final int position=(Integer) v.getTag();
        Object object= getItem(position);
        final Client client=(Client) object;
        String clientsUid = client.getUid();

        // the manager clicked on some client edit button.
        switch (v.getId()) {
            // if the manager clicked on "EDIT PROFILE"-> go to the client's profile.
            case R.id.choose_this_profile_Button:
                goToClientsProfile(clientsUid);
        }
    }


    public void goToClientsProfile(final String clientsUid){
        cRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                // go all over the books, and check- if its not null, go all over the categories-
                // and add the books to the list.
                for (DataSnapshot client : snapshot.getChildren()) {
                    Client clientProfile = snapshot.getValue(Client.class);
                    if (clientProfile.getUid() == clientsUid) {
                        System.out.println("we need to open the client's profile !!!!!!!!!!!!!!!!!!!!!!!!");
                        Intent intent = new Intent(mContext, EditClientsProfileScreen.class);
                        String theUID=clientsUid;
                        intent.putExtra(theUID,clientsUid);
                        mContext.startActivity(intent);
                    }
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }




    private int lastPosition = -1;

    // getView method used to
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Get the data item for this position
        Client client = getItem(position);

        // Check if an existing view is being reused, otherwise inflate the view
        ViewHolder viewHolder; // view lookup cache stored in tag
        final View result;

        if (convertView == null) {

            viewHolder = new ViewHolder();
            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(R.layout.edit_clients_profiles_component, parent, false);

            viewHolder.Name = (TextView) convertView.findViewById(R.id.name_profile_TextView);
            viewHolder.Email = (TextView) convertView.findViewById(R.id.email_profile_TextView);
            viewHolder.chooseButton = (Button) convertView.findViewById(R.id.choose_this_profile_Button);
            viewHolder.uid=client.getUid();

            result=convertView;
            convertView.setTag(viewHolder);

        } else {
            viewHolder = (ViewHolder) convertView.getTag();
            result=convertView;
        }

        Animation animation = AnimationUtils.loadAnimation(mContext, (position > lastPosition) ? R.anim.up_from_bottom : R.anim.down_from_top);
        result.startAnimation(animation);
        lastPosition = position;
        /**
         * we have a problem here.
         * i cant init the clients details..
         */
        viewHolder.Name.setText(client.getfullname());
        viewHolder.Email.setText(client.getEmail());
        viewHolder.chooseButton.setOnClickListener(this);
        viewHolder.chooseButton.setTag(position);
        viewHolder.uid=client.getUid();

        //Return the completed view to render on screen
        return convertView;
    }
}
