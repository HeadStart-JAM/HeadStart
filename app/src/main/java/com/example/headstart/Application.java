package com.example.headstart;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class Application extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_application);
    }

    public void returnToJobs (View v){
        Intent intent = new Intent(this, jobListingPage.class);
        startActivity(intent);
    }


}
