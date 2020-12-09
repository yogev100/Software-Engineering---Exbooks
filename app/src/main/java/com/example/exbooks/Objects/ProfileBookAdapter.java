package com.example.exbooks.Objects;

import android.content.Context;
import android.net.Uri;
import android.provider.ContactsContract;
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

import com.example.exbooks.R;
import com.example.exbooks.Screens.ProfileScreen;
import com.example.exbooks.Users.Client;
import com.example.exbooks.Users.Manager;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
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

public class ProfileBookAdapter extends ArrayAdapter<Book> implements View.OnClickListener{

    private ArrayList<Book> dataSet;
    Context mContext;
    DatabaseReference bookRef = FirebaseDatabase.getInstance().getReference("Books");
    StorageReference storageRef = FirebaseStorage.getInstance().getReference();
    DatabaseReference userRef = FirebaseDatabase.getInstance().getReference("Users");
    FirebaseAuth cAuth = FirebaseAuth.getInstance();

    // View lookup cache
    private static class ViewHolder {
        TextView bookName;
        TextView category;
        TextView author;
        TextView condition;
        ImageView bookImg;
        Button delete;
        String bid;

    }

    public void deleteFromMyBooks(final String UID, final String BID){
        final DatabaseReference clientRef = FirebaseDatabase.getInstance().getReference("Users").child("Clients");
        final DatabaseReference managerRef = FirebaseDatabase.getInstance().getReference("Users").child("Managers");

        managerRef.child(UID).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Manager m= snapshot.getValue(Manager.class);
                if(m!=null){
                    for(int i=0; i<m.getMy_books().size(); i++){
                        System.out.println(m.getMy_books().get(i)+ " @@@@@@@@@@@ " + BID);
                        if(m.getMy_books().get(i).equals(BID)){
                            System.out.println("@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@");
                            System.out.println(m.getMy_books().get(i));
                            managerRef.child(UID).child("my_books").child(i+"").removeValue();
                        }
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        clientRef.child(UID).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Client c = snapshot.getValue(Client.class);
                if(c!=null){
                    for(int i=0; i<c.getMy_books().size(); i++){
                        if(c.getMy_books().get(i).equals(BID)){
                            clientRef.child(UID).child("my_books").child(i+"").removeValue();
                        }
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    public ProfileBookAdapter(ArrayList<Book> data, Context context) {
        super(context, R.layout.book_profile, data);
        this.dataSet = data;
        this.mContext=context;

    }

    @Override
    public void onClick(View v) {
        int position=(Integer) v.getTag();
        Object object= getItem(position);
        Book book=(Book)object;

        switch (v.getId())
        {
            case R.id.button_profile:

                dataSet.remove(position);                                                   // remove from the array list
                bookRef.child(book.getCategory()).child(book.getBook_id()).removeValue();   // remove from the Books tree
                storageRef.child(book.getBook_id()+ ".jpg").delete();                       // remove the picture from the storage
                deleteFromMyBooks(cAuth.getCurrentUser().getUid(),book.getBook_id());       // remove from "my books" list in the User tree
                notifyDataSetChanged();                                                     // (remove and) update the list view..
                break;
        }
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
            convertView = inflater.inflate(R.layout.book_profile, parent, false);

            viewHolder.bookName = (TextView) convertView.findViewById(R.id.book_name_profile);
            viewHolder.category = (TextView) convertView.findViewById(R.id.category_profile);
            viewHolder.author = (TextView) convertView.findViewById(R.id.author_name);
            viewHolder.condition = (TextView) convertView.findViewById(R.id.condition_profile);
            viewHolder.delete = (Button) convertView.findViewById(R.id.button_profile);
            viewHolder.bookImg = (ImageView) convertView.findViewById(R.id.image_profile);
            viewHolder.bid=book.getBook_id();
//            viewHolder.constraint=(ConstraintLayout)convertView.findViewById(R.id.book_Form);

            result=convertView;

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ProfileBookAdapter.ViewHolder) convertView.getTag();
            result=convertView;
        }

        Animation animation = AnimationUtils.loadAnimation(mContext, (position > lastPosition) ? R.anim.up_from_bottom : R.anim.down_from_top);
        result.startAnimation(animation);
        lastPosition = position;
        viewHolder.bookName.setText(book.getBook_name());
        viewHolder.category.setText(book.getCategory());
        viewHolder.author.setText(book.getAuthor_name());
        viewHolder.condition.setText(book.condString());
        viewHolder.delete.setOnClickListener(this);
        viewHolder.delete.setTag(position);
        viewHolder.bid=book.getBook_id();
        set_url_image(position,viewHolder);
        //Return the completed view to render on screen
        return convertView;
    }

    private void set_url_image(int position, final ProfileBookAdapter.ViewHolder viewHolder){
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


