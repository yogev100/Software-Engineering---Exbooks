package com.example.exbooks.Objects;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatDialogFragment;

import com.example.exbooks.Users.User;

public class MatchDialog extends AppCompatDialogFragment {
    User user;

    public MatchDialog(User user){
        this.user=user;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState){
        AlertDialog.Builder builder=new AlertDialog.Builder(getActivity());
        builder.setTitle("Details of the match user")
                .setMessage("The phone number of "+user.getfullname()+" is "+user.getPhone()+" ,Good luck!")
                .setPositiveButton("Close", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
        return builder.create();

    }
}
