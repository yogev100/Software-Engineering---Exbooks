package com.example.exbooks.Adapters;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
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

import com.example.exbooks.Objects.Book;
import com.example.exbooks.R;
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

/**
 * This class built in order to allow us to adapt the custom view of user profile in the ProfileScreen.
 */
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

    // Method that delete the book from "my_books" list by UID,BID.
    public void deleteFromMyBooks(final String UID, final String BID){
        // takes the references
        final DatabaseReference clientRef = FirebaseDatabase.getInstance().getReference("Users").child("Clients");
        final DatabaseReference managerRef = FirebaseDatabase.getInstance().getReference("Users").child("Managers");
        // if manager
        managerRef.child(UID).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Manager m= snapshot.getValue(Manager.class);
                if(m!=null){    // manager exist
                    for(int i=0; i<m.getMy_books().size(); i++){    // go all over the books
                        if (m.getMy_books().get(i) != null) {       // check if not null
                            if (m.getMy_books().get(i).equals(BID)) {   // BID comparison
                                managerRef.child(UID).child("my_books").child(i+"").removeValue();  // remove the value from the my_books list.
                            }
                        }
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        // if client
        clientRef.child(UID).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Client c = snapshot.getValue(Client.class);
                if(c!=null){    // client exist
                    for(int i=0; i<c.getMy_books().size(); i++){    // go all over the books
                        if (c.getMy_books().get(i) != null) {       // check if not null
                            if (c.getMy_books().get(i).equals(BID)) {// BID comparison
                                clientRef.child(UID).child("my_books").child(i + "").removeValue(); // remove the value from the my_books list.
                            }
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

    // method that checks if the click is on the right button's position
    @Override
    public void onClick(View v){
        final int position=(Integer) v.getTag();
        Object object= getItem(position);
        final Book book=(Book)object;

        switch (v.getId()) {
            // if the click is on the "delete"
            case R.id.delete_button_profile:
                // make alert dialog "are you sure?"
                AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
                builder.setMessage(R.string.confirmDelete).setTitle(R.string.deleteAlert);
                // user clicks on the position button- Yes, delete.
                builder.setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dataSet.remove(position);                                                   // remove from the array list
                        bookRef.child(book.getCategory()).child(book.getBook_id()).removeValue();   // remove from the Books tree
                        if (book.getImgURL()) {                                                     // if there picture..
                            storageRef.child(book.getBook_id() + ".jpg").delete();                  // remove the picture from the storage
                        }
                        deleteFromMyBooks(cAuth.getCurrentUser().getUid(),book.getBook_id());       // remove from "my books" list in the User tree
                        notifyDataSetChanged();                                                     // (remove and) update the list view..
                        DonateCheck(book.isFor_change());
                    }
                });
                // user clicks on the negative button- No. do not do anything..
                builder.setNegativeButton(R.string.no, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // User cancelled the dialog
                    }
                });

                AlertDialog dialog = builder.create();
                dialog.show();
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
            viewHolder.delete = (Button) convertView.findViewById(R.id.delete_button_profile);
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

    // Method that gets the image url (or default..), and download the image from the storage.
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

    /*
        function that check if the deleted book is for donate and if it is - update the 'number of donated book' field in managers tree
         */
    private void DonateCheck(boolean for_change) {
        if(!for_change){ // if donate book
            final DatabaseReference numRef = FirebaseDatabase.getInstance().getReference("ManagerTools");
            numRef.child("num_of_books_donated").addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    Integer i = snapshot.getValue(Integer.class);
                    numRef.child("num_of_books_donated").setValue(--i);     // the num of books is --. because of deletion.
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        }
    }

}


