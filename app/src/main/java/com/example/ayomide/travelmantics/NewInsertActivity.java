package com.example.ayomide.travelmantics;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.ayomide.travelmantics.Common.Common;
import com.example.ayomide.travelmantics.Model.TravelDeal;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.rengwuxian.materialedittext.MaterialEditText;
import com.squareup.picasso.Picasso;

import java.util.UUID;

public class  NewInsertActivity extends AppCompatActivity {

    //Firebase
    FirebaseDatabase db;
    DatabaseReference deals_table;
    FirebaseStorage storage;
    StorageReference storageReference;

    MaterialEditText etTitle, etPrice, etDesc;
    Button btnSelect, btnUpload;
    ImageView imageView;

    TravelDeal currentDeal, newDeal;

    String dealId;

    Uri imgUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_new_insert );

        Toolbar toolbar = (Toolbar) findViewById( R.id.toolbar );
        toolbar.setTitle("Travelmantics");
        toolbar.setTitleTextColor(Color.WHITE );
        setSupportActionBar( toolbar );

        db = FirebaseDatabase.getInstance();
        deals_table = db.getReference().child( "TravelDeals" );
        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();

        etTitle = findViewById( R.id.etTitle );
        etDesc = findViewById( R.id.etDesc );
        etPrice = findViewById( R.id.etPrice );
        btnSelect = findViewById( R.id.btnSelect );
        imageView = findViewById( R.id.image_deal );

        btnSelect.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                chooseImage();
            }
        } );
    }

    private void chooseImage()
    {
        Intent intent = new Intent();
        intent.setType( "image/*" );
        intent.setAction( Intent.ACTION_GET_CONTENT );
        startActivityForResult( Intent.createChooser( intent, "Select A Picture" ), Common.IMAGE_REQUEST );
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate( R.menu.save_menu, menu );
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        switch (item.getItemId()){
            case R.id.save_menu:
                saveDeal();
                Toast.makeText( this, "Deal saved", Toast.LENGTH_SHORT ).show();
                clean();
                return true;

            case R.id.deals_activity:
                startActivity( new Intent( NewInsertActivity.this, UserActivity.class ) );
                return true;
            default:
                return super.onOptionsItemSelected( item );
        }
    }

    private void saveDeal()
    {
        String title = etTitle.getText().toString();
        String description = etDesc.getText().toString();
        String price = etPrice.getText().toString();
        Picasso.with( getBaseContext() ).load( currentDeal.getImage() ).into( imageView );
        TravelDeal deal = new TravelDeal( title, description, price, "" );
        deals_table.push().setValue( deal );
    }

    private void clean() {
        etTitle.setText( "" );
        etDesc.setText( "" );
        etPrice.setText( "" );
        etTitle.requestFocus();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult( requestCode, resultCode, data );

        if(requestCode == Common.NEW_IMAGE_REQUEST && resultCode == RESULT_OK
                && data != null && data.getData() != null)
        {
            imgUri = data.getData();
            btnSelect.setText( "Image Selected" );
            openUploadDialog();
        }
    }

    private void openUploadDialog()
    {
        final AlertDialog.Builder alertDialog = new AlertDialog.Builder( NewInsertActivity.this );
        alertDialog.setTitle( "Upload Image" );
        alertDialog.setIcon( R.drawable.ic_file_upload_black_24dp );

        LayoutInflater layoutInflater = this.getLayoutInflater();
        View view = layoutInflater.inflate( R.layout.upload_image_layout, null );
        alertDialog.setView( view );

        imageView = view.findViewById( R.id.image_deal );
        btnUpload = view.findViewById( R.id.btnImgUpload );

        btnUpload.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                uploadImage();
            }
        } );

        alertDialog.show();
    }

    private void uploadImage()
    {
        if (imgUri != null) {
            final ProgressDialog mDialog = new ProgressDialog( this );
            mDialog.setMessage( "Uploading..." );
            mDialog.show();

            String imageName = UUID.randomUUID().toString();
            final StorageReference imageFolder = storageReference.child( "dealImages/" + imageName );
            imageFolder.putFile( imgUri ).addOnSuccessListener( new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    mDialog.dismiss();
                    Toast.makeText( NewInsertActivity.this, "Uploaded!!!", Toast.LENGTH_SHORT ).show();
                    imageFolder.getDownloadUrl().addOnSuccessListener( new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {

                            newDeal = new TravelDeal();
                            newDeal.setTitle( etTitle.getText().toString() );
                            newDeal.setDescription( etDesc.getText().toString() );
                            newDeal.setPrice( etPrice.getText().toString() );
                            newDeal.setImage( uri.toString() );

                            deals_table.push().setValue( newDeal );
                            Toast.makeText( NewInsertActivity.this, "uploaded", Toast.LENGTH_SHORT )
                                    .show();
                        }
                    } );
                }
            } ).addOnFailureListener( new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    mDialog.dismiss();
                    Toast.makeText( NewInsertActivity.this, "" + e.getMessage(), Toast.LENGTH_SHORT ).show();
                }
            } ).addOnProgressListener( new OnProgressListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                    double progress = (100.0 * taskSnapshot.getBytesTransferred() / taskSnapshot.getTotalByteCount());
                    mDialog.setMessage( "Uploaded " + progress + "%" );
                }
            } );
        }
    }
}
