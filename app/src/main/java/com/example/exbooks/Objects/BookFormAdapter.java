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
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.FragmentManager;

import com.example.exbooks.R;
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

public class BookFormAdapter extends  ArrayAdapter<Book> implements View.OnClickListener{

    private ArrayList<Book> dataSet;
    Context mContext;
    String screen;
    FragmentManager supportFragmentManager;


    // View lookup cache
    private static class ViewHolder {
        ConstraintLayout constraint;
        TextView bookName;
        TextView category;
        TextView author;
        TextView cond;
        TextView city;
        ImageView bookImg;
        Button chooseButton;
        String bid;

    }

    public BookFormAdapter(ArrayList<Book> data, Context context, String screen, FragmentManager supportFragmentManager) {
        super(context, R.layout.book_search_component, data);
        //super(context, R.layout.single_book, data);
        this.dataSet = data;
        this.mContext=context;
        this.screen=screen;
        this.supportFragmentManager=supportFragmentManager;

    }

    @Override
    public void onClick(View v) {
        int position=(Integer) v.getTag();
        Object object= getItem(position);
        Book book=(Book)object;

        switch (v.getId())
        {
            case R.id.bookId_Button:
                System.out.println(screen);
                if(screen.equals("search")) {
                    sendNotification(book, true);
                    Snackbar.make(v, "Book request sent to the book owner", Snackbar.LENGTH_LONG)
                            .setAction("No action", null).show();
                    v.setClickable(false);
                    break;
                }else if(screen.equals("match")){
                    sendNotification(book, false);
                    openPhoneDialog(book.getUid());
					
                }
        }
    }

    private void openPhoneDialog(String otherUserId) {
        DatabaseReference mRef= FirebaseDatabase.getInstance().getReference("Users").child("Managers");
        DatabaseReference cRef = FirebaseDatabase.getInstance().getReference("Users").child("Clients");

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

    private void sendNotification(final Book book, final boolean first) {
        final DatabaseReference managerRef= FirebaseDatabase.getInstance().getReference("Users").child("Managers");
        final DatabaseReference clientRef= FirebaseDatabase.getInstance().getReference("Users").child("Managers");
        final String[] sentNotificationName = new String[1];

        managerRef.child(FirebaseAuth.getInstance().getCurrentUser().getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Manager m = snapshot.getValue(Manager.class);
                if(m!=null){
                    sentNotificationName[0] =m.getfullname();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        clientRef.child(FirebaseAuth.getInstance().getCurrentUser().getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Client c = snapshot.getValue(Client.class);
                if(c!=null){
                    sentNotificationName[0] =c.getfullname();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        managerRef.child(book.getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Manager m = snapshot.getValue(Manager.class);
                if(m!=null){
                    m.getNotification().add(new Notification(FirebaseAuth.getInstance().getCurrentUser().getUid(),sentNotificationName[0],book,first));
                    managerRef.child(book.getUid()).setValue(m);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        clientRef.child(book.getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Client c = snapshot.getValue(Client.class);
                if(c!=null){
                    c.getNotification().add(new Notification(FirebaseAuth.getInstance().getCurrentUser().getUid(),sentNotificationName[0],book,first));
                    managerRef.child(book.getUid()).setValue(c);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


    }

    private int lastPosition = -1;

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Get the data item for this position
        Book book = getItem(position);

        // Check if an existing view is being reused, otherwise inflate the view
        ViewHolder viewHolder; // view lookup cache stored in tag

        final View result;

        if (convertView == null) {

            viewHolder = new ViewHolder();
            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(R.layout.book_search_component, parent, false);

            viewHolder.bookName = (TextView) convertView.findViewById(R.id.nameBookId_TextView);
            viewHolder.category = (TextView) convertView.findViewById(R.id.categoryBookId_TextView);
            viewHolder.author = (TextView) convertView.findViewById(R.id.authorBookId_TextView);
            viewHolder.cond = (TextView) convertView.findViewById(R.id.conditionBookId_TextView);
            viewHolder.city = (TextView) convertView.findViewById(R.id.cityBookId_TextView);
            viewHolder.chooseButton = (Button) convertView.findViewById(R.id.bookId_Button);
            viewHolder.bookImg = (ImageView) convertView.findViewById(R.id.ImageBookId_ImageView);
            viewHolder.bid=book.getBook_id();

            result=convertView;

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
            result=convertView;
        }

        Animation animation = AnimationUtils.loadAnimation(mContext, (position > lastPosition) ? R.anim.up_from_bottom : R.anim.down_from_top);
        result.startAnimation(animation);
        lastPosition = position;

        viewHolder.bookName.setText(book.getBook_name());
        viewHolder.category.setText(book.getCategory());
        viewHolder.author.setText(book.getAuthor_name());
        viewHolder.cond.setText(book.cond_toString(book.getBook_cond()));
        viewHolder.city.setText(book.getCityOwner());
        viewHolder.chooseButton.setOnClickListener(this);
        viewHolder.chooseButton.setTag(position);
        viewHolder.bid=book.getBook_id();
        set_url_image(position,viewHolder);
        //Return the completed view to render on screen
        return convertView;
    }

    private void set_url_image(int position, final ViewHolder viewHolder){
        final StorageReference storageRef = FirebaseStorage.getInstance().getReference();
        String book_img_name = "";
        if(dataSet.get(position).getImgURL()){
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


