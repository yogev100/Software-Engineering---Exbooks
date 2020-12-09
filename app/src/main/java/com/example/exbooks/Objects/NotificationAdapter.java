package com.example.exbooks.Objects;

import android.content.Context;
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
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.example.exbooks.R;
import com.example.exbooks.Screens.MainActivity;
import com.example.exbooks.Screens.SearchResultScreen;
import com.example.exbooks.Users.Client;
import com.example.exbooks.Users.Manager;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class NotificationAdapter extends  ArrayAdapter<Notification> implements View.OnClickListener{

    private ArrayList<Notification> dataSet;
    Context mContext;

    // View lookup cache
    private static class ViewHolder {
        TextView ownerName;
        TextView notification_txt;
        ImageView bookImg;
        Button checkButton;
        String bid;
        String wanterName;

    }

    public NotificationAdapter(ArrayList<Notification> data, Context context) {
        super(context, R.layout.notification_view, data);
        //super(context, R.layout.single_book, data);
        this.dataSet = data;
        this.mContext=context;

    }

    @Override
    public void onClick(View v) {
        int position=(Integer) v.getTag();
        Object object= getItem(position);
        Notification notification=(Notification)object;

        switch (v.getId())
        {
            case R.id.bookId_Button:
                sendNotificatio();
                Snackbar.make(v, "Book request sent to the book owner", Snackbar.LENGTH_LONG)
                        .setAction("No action", null).show();
                break;
        }
    }

    private void sendNotificatio() {

    }

    private int lastPosition = -1;

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
            viewHolder.bid=notification.getBook().getBook_id();
            viewHolder.wanterName=notification.getWanterName();


            result=convertView;

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
            result=convertView;
        }

        Animation animation = AnimationUtils.loadAnimation(mContext, (position > lastPosition) ? R.anim.up_from_bottom : R.anim.down_from_top);
        result.startAnimation(animation);
        lastPosition = position;

        viewHolder.notification_txt.setText("wants your book-"+notification.getBook().getBook_name()+", check what books he can offer you.");
        viewHolder.ownerName.setText(notification.getWanterName()+",");
        viewHolder.checkButton.setOnClickListener(this);
        viewHolder.checkButton.setTag(position);
        viewHolder.bid=notification.getBook().getBook_id();
        set_url_image(position,viewHolder);
        //Return the completed view to render on screen
        return convertView;
    }

    private void set_url_image(int position, final ViewHolder viewHolder){
        final StorageReference storageRef = FirebaseStorage.getInstance().getReference();
        String book_img_name = "";
        if(dataSet.get(position).getBook().getImgURL()){
            book_img_name = viewHolder.bid+".jpg";
        }
        else{
            book_img_name = "no_image.png";
        }
        StorageReference img_ref = storageRef.child(book_img_name);
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


