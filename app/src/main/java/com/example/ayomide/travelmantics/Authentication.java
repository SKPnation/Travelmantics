package com.example.ayomide.travelmantics;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;

public class Authentication extends AppCompatActivity {

    private Button btnEmail, btnGoogle;

    @SuppressLint("ResourceAsColor")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_authentication );

        Toolbar toolbar = (Toolbar) findViewById( R.id.toolbar );
        toolbar.setTitle("Travelmantics");
        toolbar.setTitleTextColor(Color.WHITE );
        setSupportActionBar( toolbar );

        btnEmail = findViewById( R.id.btnEmailSignIn );
        btnGoogle = findViewById( R.id.btnGoogleSignIn );

        btnEmail.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity( new Intent( Authentication.this, SignUp.class ) );
            }
        } );
    }
}
