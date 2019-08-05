package com.example.ayomide.travelmantics;

import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;

public class SignUp extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_sign_up );

        Toolbar toolbar = (Toolbar) findViewById( R.id.toolbar );
        toolbar.setTitle("Sign up");
        toolbar.setTitleTextColor( Color.WHITE );
        setSupportActionBar( toolbar );
    }
}
