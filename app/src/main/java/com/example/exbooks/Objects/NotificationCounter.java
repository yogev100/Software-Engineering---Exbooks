package com.example.exbooks.Objects;

import android.util.Log;
import android.view.View;
import android.widget.TextView;

import androidx.cardview.widget.CardView;

import com.example.exbooks.R;

/**
 * This class responsible for the notification bell counter
 */
public class NotificationCounter {
    private TextView notificationNumber;
    private final int MAX_NUMBER = 99;
    private int notification_number_counter = 0;

    // Constructor
    public NotificationCounter(View view,int num){
        notificationNumber=view.findViewById(R.id.notificationNumber);

        if(num<MAX_NUMBER) {
            notification_number_counter = num;
        }else {
            notification_number_counter = MAX_NUMBER;
        }
        notificationNumber.setText(String.valueOf(notification_number_counter));
        CardView cv = view.findViewById(R.id.notificationNumberContainer);
        System.out.println("asdasdas"+num);
        if(num==0){
            cv.setVisibility(View.GONE);
            notificationNumber.setVisibility(View.GONE);
        }
        else {
            cv.setVisibility(View.VISIBLE);
            notificationNumber.setVisibility(View.VISIBLE);
        }
    }
}
