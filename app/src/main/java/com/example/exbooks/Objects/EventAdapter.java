package com.example.exbooks.Objects;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
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

/**
 * This class built in order to allow us to adapt the custom view of book event in the search screen.
 */
public class EventAdapter extends ArrayAdapter<Book> implements View.OnClickListener{

    private ArrayList<Book> dataSet;
    Context mContext;
    final int MinBookNumForEvent = 2;
    static int num_checked;
    private static ArrayList<Book> selected_books;

    // View lookup cache
    private static class ViewHolder {
        TextView bookName;
        TextView category;
        TextView author;
        TextView cond;
        TextView city;
        ImageView bookImg;
        CheckBox choose_box;
        String bid;

    }

    public EventAdapter(ArrayList<Book> data, Context context) {
        super(context, R.layout.event_book_component, data);
        this.dataSet = data;
        this.mContext=context;
        num_checked = 0;
        selected_books = new ArrayList<>();
    }

    @Override
    public void onClick(View v) {
        int position=(Integer) v.getTag();
        Object object= getItem(position);
        Book book=(Book)object;
//        viewHolder.choose_box.seton;
//        switch (v.getId())
//        {
//            case R.id.create_event_button:
//                if(num_checked>=MinBookNumForEvent) {
//                    Snackbar.make(v, "Book request sent to the book owner", Snackbar.LENGTH_LONG)
//                            .setAction("No action", null).show();
//                    v.setClickable(false);
//                    break;
//                }
//                else{
//
//                }
//        }
    }

    private int lastPosition = -1;

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        // Get the data item for this position
        final Book book = getItem(position);

        // Check if an existing view is being reused, otherwise inflate the view
        ViewHolder viewHolder; // view lookup cache stored in tag

        final View result;

        if (convertView == null) {

            viewHolder = new ViewHolder();
            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(R.layout.event_book_component, parent, false);

            viewHolder.bookName = (TextView) convertView.findViewById(R.id.eventbook_name);
            viewHolder.category = (TextView) convertView.findViewById(R.id.category_event_name);
            viewHolder.author = (TextView) convertView.findViewById(R.id.author_event_name);
            viewHolder.cond = (TextView) convertView.findViewById(R.id.cond_event_name);
            viewHolder.city = (TextView) convertView.findViewById(R.id.city_event_name);
            viewHolder.choose_box = (CheckBox) convertView.findViewById(R.id.checkbox_event);
            viewHolder.bookImg = (ImageView) convertView.findViewById(R.id.img_event);


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
        viewHolder.bid=book.getBook_id();
        set_url_image(position,viewHolder);
        viewHolder.choose_box.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    num_checked++;
                    selected_books.add(new Book(dataSet.get(position)));

                }
                else{
                    num_checked--;
                    selected_books.remove(book);
                }
            }
        });
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

    public static int getNum_checked(){
        return num_checked;
    }

    public static ArrayList<Book> getSelectedBooks(){
        return selected_books;
    }

}


