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
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.lifecycle.AndroidViewModel;

import com.example.exbooks.R;
import com.example.exbooks.Screens.ProfileScreen;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class ProfileBookAdapter extends ArrayAdapter<Book> implements View.OnClickListener{

    private ArrayList<Book> dataSet;
    Context mContext;
    DatabaseReference bookRef = FirebaseDatabase.getInstance().getReference("Books");

    // View lookup cache
    private static class ViewHolder {
        TextView bookName;
        ImageView bookImg;
        Button deleteButton;
        String bid;
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

        switch (v.getId()){
            case R.id.button_profile:
                deleteTheBook(book);
                break;
        }
    }

    private void deleteTheBook(Book book){
        bookRef.child(book.getCategory()).child(book.getBook_id()).removeValue();
        for(int i=0; i<dataSet.size();i++){
            if(dataSet.get(i).getBook_id() == book.getBook_id()){
                dataSet.remove(i);
                Toast.makeText(getContext(), "The book deleted", Toast.LENGTH_LONG).show();
                break;
            }
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
            viewHolder.deleteButton = (Button) convertView.findViewById(R.id.button_profile);
            viewHolder.bookImg = (ImageView) convertView.findViewById(R.id.image_profile);
            viewHolder.bid=book.getBook_id();

            result=convertView;

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
            result=convertView;
        }

        Animation animation = AnimationUtils.loadAnimation(mContext, (position > lastPosition) ? R.anim.right_to_left : R.anim.left_to_right);
        result.startAnimation(animation);
        lastPosition = position;

        viewHolder.bookName.setText(book.getBook_name());
        viewHolder.deleteButton.setOnClickListener(this);
        viewHolder.bookImg.setTag(position);
        viewHolder.bid=book.getBook_id();
        set_url_image(position,viewHolder);
        //Return the completed view to render on screen
        return convertView;
    }

    private void set_url_image(int position, final ViewHolder viewHolder){
        final StorageReference storageRef = FirebaseStorage.getInstance().getReference();
        String book_img_name = "";
        if(dataSet.get(position).getImgURL()){
            System.out.println(viewHolder.bid);
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


