package com.example.exbooks.Screens;

import android.os.Bundle;
import android.widget.ScrollView;

import androidx.appcompat.app.AppCompatActivity;
import com.example.exbooks.R;


public class SearchResultScreen extends AppCompatActivity {

    ScrollView sv;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.search_result_screen);

        sv=(ScrollView)findViewById(R.id.search_scrollView);

    }
}
