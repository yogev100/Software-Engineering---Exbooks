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
import androidx.constraintlayout.widget.ConstraintLayout;

import com.example.exbooks.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class BookFormAdapter extends ArrayAdapter<Book> implements View.OnClickListener{

    private ArrayList<Book> dataSet;
    Context mContext;

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
    }

    public BookFormAdapter(ArrayList<Book> data, Context context) {
        super(context, R.layout.single_book, data);
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
            convertView = inflater.inflate(R.layout.single_book, parent, false);

            viewHolder.bookName = (TextView) convertView.findViewById(R.id.single_book_name);
            viewHolder.category = (TextView) convertView.findViewById(R.id.single_book_category);
            viewHolder.author = (TextView) convertView.findViewById(R.id.single_book_author);
            viewHolder.cond = (TextView) convertView.findViewById(R.id.single_book_cond);
            viewHolder.city = (TextView) convertView.findViewById(R.id.single_book_city);
            viewHolder.chooseButton = (Button) convertView.findViewById(R.id.single_book_button);
            viewHolder.bookImg = (ImageView) convertView.findViewById(R.id.single_book_img);
//            viewHolder.constraint=(ConstraintLayout)convertView.findViewById(R.id.book_Form);

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
        viewHolder.bookImg.setTag(position);
        set_url_image(position,viewHolder);
        //Return the completed view to render on screen
        return convertView;
    }

    private void set_url_image(int position, final ViewHolder viewHolder){
        final StorageReference storageRef = FirebaseStorage.getInstance().getReference();
        String book_img_name = "";
        if(dataSet.get(position).getImgURL()){
            book_img_name = dataSet.get(position).getBook_id()+".jpg";
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


