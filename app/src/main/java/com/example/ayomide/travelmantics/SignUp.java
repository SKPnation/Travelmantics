package com.example.ayomide.travelmantics;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

public class SignUp extends AppCompatActivity {

    FirebaseAuth mAuth;
    DatabaseReference user;

    String currentUserID;

    ProgressDialog loadingBar;

    TextInputEditText etEmail, etName, etPassword;
    Button signUp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_sign_up );

        Toolbar toolbar = (Toolbar) findViewById( R.id.toolbar );
        toolbar.setTitle("Sign up");
        toolbar.setTitleTextColor( Color.WHITE );
        setSupportActionBar( toolbar );

        mAuth = FirebaseAuth.getInstance();
        user = FirebaseDatabase.getInstance().getReference();

        InitializeFields();

        signUp = findViewById( R.id.btnSignUp );
        signUp.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CreateNewAccount();
            }
        } );
    }

    private void InitializeFields()
    {
        signUp = findViewById( R.id.btnSignUp );
        etEmail = findViewById( R.id.text_input_email );
        etName = findViewById( R.id.text_input_name );
        etPassword = findViewById( R.id.text_input_password );

        loadingBar = new ProgressDialog(this);
    }

    private void CreateNewAccount()
    {
        final String email = etEmail.getText().toString();
        final String name = etName.getText().toString();
        final String password = etPassword.getText().toString();

        if (!validateInputs( email, name, password))
        {
            loadingBar.setTitle("Creating New Account");
            loadingBar.setMessage("Please wait, while we are creating a new account for you...");
            loadingBar.setCanceledOnTouchOutside(true);
            loadingBar.show();

            mAuth.createUserWithEmailAndPassword( email, password )
                    .addOnCompleteListener( new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if(task.isSuccessful())
                            {
                                currentUserID = mAuth.getCurrentUser().getUid();

                                HashMap<String, String> ProfileMap = new HashMap<>();
                                ProfileMap.put("email", email);
                                ProfileMap.put("name", name);
                                ProfileMap.put("password", password);

                                user.child( "Users" ).child(currentUserID).setValue(ProfileMap).addOnCompleteListener( new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if (task.isSuccessful())
                                        {
                                            startActivity( new Intent( SignUp.this, UserActivity.class ) );
                                            Toast.makeText(SignUp.this, "Account created successfully", Toast.LENGTH_SHORT).show();
                                            loadingBar.dismiss();
                                        }
                                        else
                                        {
                                            String message = task.getException().toString();
                                            Toast.makeText( SignUp.this, "Error : " + message, Toast.LENGTH_SHORT ).show();
                                        }
                                    }
                                } );
                            }
                            else
                            {
                                String message = task.getException().toString();
                                Toast.makeText( SignUp.this, "Error : " + message, Toast.LENGTH_SHORT ).show();
                                loadingBar.dismiss();
                            }
                        }
                    } );
        }

    }

    private boolean validateInputs(String email, String name, String password)
    {
        if (email.isEmpty()) {
            etEmail.setError("email required");
            etEmail.requestFocus();
            return true;
        }

        if (name.isEmpty()) {
            etName.setError("name required");
            etName.requestFocus();
            return true;
        }

        if (password.isEmpty()) {
            etPassword.setError("password required");
            etPassword.requestFocus();
            return true;
        }

        return false;
    }
}
