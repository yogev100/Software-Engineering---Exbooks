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

import com.example.exbooks.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class ProfileBookAdapter extends ArrayAdapter<Book> implements View.OnClickListener{

    private ArrayList<Book> dataSet;
    Context mContext;

    // View lookup cache
    private static class ViewHolder {
        TextView bookName;
        ImageView bookImg;
        Button delete;
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

        switch (v.getId())
        {
            case R.id.bookId_Button:
                //Snackbar.make(v, "Release date " +book.getFeature(), Snackbar.LENGTH_LONG)
                //.setAction("No action", null).show();
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
        viewHolder.delete.setOnClickListener(this);
        viewHolder.bookImg.setTag(position);
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


