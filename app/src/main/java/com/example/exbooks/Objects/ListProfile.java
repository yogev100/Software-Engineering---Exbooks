package com.example.exbooks.Objects;

import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.exbooks.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import java.util.List;

public class ListProfile extends RecyclerView.Adapter<ListProfile.MyViewHolder> {
    private List<Book> booksList;
    public  static class MyViewHolder extends RecyclerView.ViewHolder {
        TextView bookName;
        Button delete;
        static ImageView img;
        static String bid;

        MyViewHolder(View view) {
            super(view);
            bookName = view.findViewById(R.id.book_name_profile);
            delete = view.findViewById(R.id.button_profile);
            img = view.findViewById(R.id.image_profile);
        }
    }
    public ListProfile(List<Book> moviesList) {
        this.booksList = moviesList;
    }
    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.book_profile, parent, false);
        return new MyViewHolder(itemView);
    }
    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        Book book = booksList.get(position);
        MyViewHolder.bid = book.getBook_id();
        System.out.println(booksList.get(position).getBook_name());
        holder.bookName.setText(book.getBook_name());
//        holder.delete.setText(book.getGenre());
        set_url_image(position);
    }
    @Override
    public int getItemCount() {
        return booksList.size();
    }



    private void set_url_image(int position){
        final StorageReference storageRef = FirebaseStorage.getInstance().getReference();
        String book_img_name = "";
        if(booksList.get(position).getImgURL()){
            book_img_name = MyViewHolder.bid+".jpg";
        }
        else{
            book_img_name = "no_image.png";
        }
        StorageReference img_ref = storageRef.child(book_img_name);
        img_ref.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                Picasso.get().load(uri).into(MyViewHolder.img);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

            }
        });

    }
}