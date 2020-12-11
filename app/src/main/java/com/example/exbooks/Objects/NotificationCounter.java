package com.example.exbooks.Objects;

import android.util.Log;
import android.view.View;
import android.widget.TextView;
import com.example.exbooks.R;


public class NotificationCounter {
    private TextView notificationNumber;
    private final int MAX_NUMBER = 99;
    private int notification_number_counter = 0;

    public NotificationCounter(View view,int num){
        System.out.println(num+"in");
        notificationNumber=view.findViewById(R.id.notificationNumber);
        notification_number_counter=num;
        notificationNumber.setText(String.valueOf(notification_number_counter));


    }

    public void increaseNumber(int num){
        notification_number_counter=num;

        if(notification_number_counter>MAX_NUMBER){
            Log.d("Counter","Maximum Number Reached!");
        }else{
            notificationNumber.setText(String.valueOf(notification_number_counter));
        }
    }
}
