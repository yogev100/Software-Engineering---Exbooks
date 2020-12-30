package com.example.exbooks.Objects;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatDialogFragment;

import com.example.exbooks.Users.User;

/**
 * This class represent an event dialog
 */
public class EventDialog extends AppCompatDialogFragment {
    User user;

    public EventDialog(User user){
        this.user=user;
    }


    /**
     * setting the dialog text and title and show it
     * @param savedInstanceState
     * @return
     */
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState){
        AlertDialog.Builder builder=new AlertDialog.Builder(getActivity());
        builder.setTitle("User's book details")
                .setMessage("The phone number of "+user.getfullname()+" is "+user.getPhone()+" ,Good luck!")
                .setPositiveButton("Close", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
        return builder.create();

    }
}
