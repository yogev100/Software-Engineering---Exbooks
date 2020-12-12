package com.example.exbooks.Adapters;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;

import com.example.exbooks.Objects.MatchDialog;
import com.example.exbooks.Objects.Notification;
import com.example.exbooks.R;
import com.example.exbooks.Screens.MaybeMatch;
import com.example.exbooks.Screens.NotificationScreen;
import com.example.exbooks.Users.Client;
import com.example.exbooks.Users.Manager;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;


/**
 * This class built in order to allow us to adapt the custom view of user notification in the NotificationScreen.
 */
public class NotificationAdapter extends  ArrayAdapter<Notification> implements View.OnClickListener{

    private ArrayList<Notification> dataSet;
    Context mContext;
    FragmentManager supportFragmentManager;


    // View lookup cache
    private static class ViewHolder {
        TextView ownerName;
        TextView notification_txt;
        ImageView bookImg;
        Button checkButton;
        String bid;
        String wanterName;

    }
    // Constructor
    public NotificationAdapter(ArrayList<Notification> data, Context context, FragmentManager supportFragmentManager) {
        super(context, R.layout.notification_view, data);
        this.dataSet = data;
        this.mContext=context;
        this.supportFragmentManager=supportFragmentManager;

    }

    // onClick method used to begin the action
    @Override
    public void onClick(View v) {
        // get the click position and take the object.
        int position=(Integer) v.getTag();
        Object object= getItem(position);
        Notification notification =(Notification)object;

        // check if clicked at the button, at the right position..
        switch (v.getId()) {
            case R.id.check_Button:
                // if its the first person who wants.
                if(notification.isFirst()) {
                    Intent intent = new Intent(mContext, MaybeMatch.class);
                    intent.putExtra("wanterID", notification.getUserWantsTheBookId());
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    mContext.startActivity(intent);
                    break;
                }else{ // its the second person who wants.
                    openPhoneDialog(notification.getUserWantsTheBookId());
                }
        }
    }

    // Method that shows the match dialog
    private void openPhoneDialog(String otherUserId) {
        DatabaseReference mRef= FirebaseDatabase.getInstance().getReference("Users").child("Managers");
        DatabaseReference cRef = FirebaseDatabase.getInstance().getReference("Users").child("Clients");

        //opens dialog with other user info(phone, name)
        mRef.child(otherUserId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Manager m = snapshot.getValue(Manager.class);
                if(m!=null){
                    MatchDialog md = new MatchDialog(m);
                    md.show(supportFragmentManager,"Match dialog");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        cRef.child(otherUserId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Client c = snapshot.getValue(Client.class);
                if(c!=null){
                    MatchDialog md = new MatchDialog(c);
                    md.show(supportFragmentManager,"Match dialog");
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
        Notification notification = getItem(position);

        // Check if an existing view is being reused, otherwise inflate the view
        ViewHolder viewHolder; // view lookup cache stored in tag

        final View result;

        if (convertView == null) {

            viewHolder = new ViewHolder();
            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(R.layout.notification_view, parent, false);

            viewHolder.ownerName = (TextView) convertView.findViewById(R.id.ownerId_notifi_textview);
            viewHolder.notification_txt = (TextView) convertView.findViewById(R.id.conditionBookId_TextView1);
            viewHolder.checkButton = (Button) convertView.findViewById(R.id.check_Button);
            viewHolder.bookImg = (ImageView) convertView.findViewById(R.id.ImageBookId_ImageView1);
            viewHolder.bid= notification.getBook().getBook_id();
            viewHolder.wanterName= notification.getWanterName();


            result=convertView;

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
            result=convertView;
        }

        Animation animation = AnimationUtils.loadAnimation(mContext, (position > lastPosition) ? R.anim.up_from_bottom : R.anim.down_from_top);
        result.startAnimation(animation);
        lastPosition = position;

        // if its the first person who wants, make this nofitication-
        if(notification.isFirst()) {
            viewHolder.notification_txt.setText("wants your book-" + notification.getBook().getBook_name() + ", check what books he can offer you.");
            viewHolder.ownerName.setText(notification.getWanterName() + ",");
            viewHolder.checkButton.setOnClickListener(this);
            viewHolder.checkButton.setText("Check");
            viewHolder.checkButton.setTag(position);
            viewHolder.bid = notification.getBook().getBook_id();
        }else{ // its the second person who wants, make Match..
            viewHolder.notification_txt.setText("if you want to exchange the book "+notification.getBook().getBook_name()+" with "+notification.getWanterName()+" click"  );
            viewHolder.ownerName.setText("M-a-t-c-h !!");
            viewHolder.checkButton.setOnClickListener(this);
            viewHolder.checkButton.setText("Exchange");
            viewHolder.checkButton.setTag(position);
            viewHolder.bid = notification.getBook().getBook_id();
        }
        set_url_image(position,viewHolder);
        //Return the completed view to render on screen
        return convertView;
    }

    // Method to take the image from the storage..
    private void set_url_image(int position, final ViewHolder viewHolder){
        final StorageReference storageRef = FirebaseStorage.getInstance().getReference();
        String book_img_name = "";
        // check if there some picture, else take the default..
        if(dataSet.get(position).getBook().getImgURL()){
            book_img_name = viewHolder.bid+".jpg";
        }
        else{
            book_img_name = "no_image.png";
        }
        // make the reference to this book
        StorageReference img_ref = storageRef.child(book_img_name);
        // and download
        img_ref.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                Picasso.get().load(uri).into(viewHolder.bookImg);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

            }
        });

    }

}


