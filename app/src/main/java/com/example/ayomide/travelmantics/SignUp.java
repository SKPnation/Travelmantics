package com.example.ayomide.travelmantics;

import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;

public class SignUp extends AppCompatActivity {

    Button signUp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_sign_up );

        Toolbar toolbar = (Toolbar) findViewById( R.id.toolbar );
        toolbar.setTitle("Sign up");
        toolbar.setTitleTextColor( Color.WHITE );
        setSupportActionBar( toolbar );

        signUp = findViewById( R.id.btnSignUp );
        signUp.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity( new Intent( SignUp.this, UserActivity.class ) );
            }
        } );
    }
}
