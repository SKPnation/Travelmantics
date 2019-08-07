package com.example.ayomide.travelmantics;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.rengwuxian.materialedittext.MaterialEditText;

public class Login extends AppCompatActivity {

    FirebaseAuth mAuth;
    FirebaseUser currentUser;

    Button btnLogin;
    TextInputEditText etEmail, etPassword;

    ProgressDialog loadingBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_login );

        Toolbar toolbar = (Toolbar) findViewById( R.id.toolbar );
        toolbar.setTitle("Log in");
        toolbar.setTitleTextColor( Color.WHITE );
        setSupportActionBar( toolbar );

        mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser();

        etEmail = findViewById( R.id.text_input_email );
        etPassword = findViewById( R.id.text_input_password );
        btnLogin = findViewById( R.id.btnLogIn );

        loadingBar = new ProgressDialog( this);

        btnLogin.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AllowUserToLogin();
            }
        } );
    }


    @Override
    protected void onStart()
    {
        super.onStart();

        //if user isn't authenticated
        if(currentUser != null)
        {
            VerifyUserExistence();
        }
    }

    private void VerifyUserExistence()
    {
        final String currentUserID = mAuth.getCurrentUser().getUid();

        /*under the parent node which is Users, we have different IDs for different users and under
         that currentUserID we'll have name and status */
        DatabaseReference user_table = FirebaseDatabase.getInstance().getReference();
        user_table.child("Users").child(currentUserID).addValueEventListener( new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if((dataSnapshot.child("name").exists()))
                {
                    etEmail.setText( dataSnapshot.child( "email" ).getValue().toString() );
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void AllowUserToLogin()
    {
        String email = etEmail.getText().toString();
        String password = etPassword.getText().toString();

        if(TextUtils.isEmpty(email))
        {
            Toast.makeText(this, "Please enter email...", Toast.LENGTH_SHORT).show();
        }
        if(TextUtils.isEmpty(password))
        {
            Toast.makeText(this, "Please enter password...", Toast.LENGTH_SHORT).show();
        }
        else
        {
            loadingBar.setTitle("Sign In");
            loadingBar.setMessage("Please wait...");
            loadingBar.setCanceledOnTouchOutside(true);
            loadingBar.show();

            mAuth.signInWithEmailAndPassword( email, password )
                    .addOnCompleteListener( new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful())
                            {
                                startActivity( new Intent( Login.this, UserActivity.class ) );
                                finish();
                                loadingBar.dismiss();
                            }
                            else
                            {
                                startActivity( new Intent( Login.this, SignUp.class ) );
                                Toast.makeText(Login.this, "Account doesn't exist", Toast.LENGTH_SHORT).show();
                                loadingBar.dismiss();
                            }
                        }
                    } );
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate( R.menu.new_account_menu, menu );
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        switch (item.getItemId()){
            case R.id.create_account:
                startActivity( new Intent( Login.this, SignUp.class ) );
                return true;
            default:
                return super.onOptionsItemSelected( item );
        }
    }
}
