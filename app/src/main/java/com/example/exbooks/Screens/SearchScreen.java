package com.example.exbooks.Screens;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.example.exbooks.R;

public class SearchScreen extends AppCompatActivity {
    public String category;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
    }
}