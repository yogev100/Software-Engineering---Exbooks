package com.example.exbooks.Objects;

import com.example.exbooks.R;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.constraintlayout.widget.ConstraintLayout;

import com.google.android.material.snackbar.Snackbar;

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
        super(context, R.layout.book_search_component, data);
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
            convertView = inflater.inflate(R.layout.book_search_component, parent, false);

            viewHolder.bookName = (TextView) convertView.findViewById(R.id.nameBookId_TextView);
            viewHolder.category = (TextView) convertView.findViewById(R.id.categoryBookId_TextView);
            viewHolder.author = (TextView) convertView.findViewById(R.id.authorBookId_TextView);
            viewHolder.cond = (TextView) convertView.findViewById(R.id.conditionBookId_TextView);
            viewHolder.city = (TextView) convertView.findViewById(R.id.cityBookId_TextView);
            viewHolder.chooseButton = (Button) convertView.findViewById(R.id.bookId_Button);
            viewHolder.bookImg = (ImageView) convertView.findViewById(R.id.ImageBookId_ImageView);
            viewHolder.constraint=(ConstraintLayout)convertView.findViewById(R.id.book_Form);

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
        //Return the completed view to render on screen
        return convertView;
    }
}
