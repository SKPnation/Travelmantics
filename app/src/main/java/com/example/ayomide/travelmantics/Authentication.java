package com.example.ayomide.travelmantics;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class Authentication extends AppCompatActivity {

    private Button btnEmail, btnGoogle;
    private TextView new_member, already_member;

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
                showDialog();
            }
        } );
    }

    private void showDialog()
    {
        final AlertDialog.Builder alertDialog = new AlertDialog.Builder( Authentication.this );
        alertDialog.setTitle( "Pick one" );

        LayoutInflater layoutInflater = this.getLayoutInflater();
        View view = layoutInflater.inflate( R.layout.account_check_layout, null );
        alertDialog.setView( view );

        new_member = view.findViewById( R.id.new_member );
        already_member = view.findViewById( R.id.already_member );

        new_member.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity( new Intent( Authentication.this, SignUp.class ) );
            }
        } );

        already_member.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //...
            }
        } );

        alertDialog.show();
    }
}
