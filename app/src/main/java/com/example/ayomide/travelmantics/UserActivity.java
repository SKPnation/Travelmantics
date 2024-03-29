package com.example.ayomide.travelmantics;

import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.example.ayomide.travelmantics.Model.TravelDeal;
import com.example.ayomide.travelmantics.ViewHolder.DealViewHolder;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

public class UserActivity extends AppCompatActivity {

    FirebaseDatabase db;
    DatabaseReference mDatabaseReference;
    FirebaseRecyclerAdapter<TravelDeal, DealViewHolder> adapter;

    FirebaseUser currentUser;
    FirebaseAuth mAuth;

    //View
    RecyclerView recycler_deals;
    RecyclerView.LayoutManager layoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_user );

        Toolbar toolbar = (Toolbar) findViewById( R.id.toolbar );
        toolbar.setTitle("Travelmantics");
        toolbar.setTitleTextColor(Color.WHITE );
        setSupportActionBar( toolbar );

        mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser();

        db = FirebaseDatabase.getInstance();
        mDatabaseReference = db.getReference().child( "TravelDeals" );

        //load deals list
        recycler_deals = findViewById( R.id.recycler_deals );
        recycler_deals.setHasFixedSize( true );
        layoutManager = new LinearLayoutManager( this );
        recycler_deals.setLayoutManager( layoutManager );

        loadDealsList();
    }

    @Override
    protected void onStart()
    {
        super.onStart();

        //if user isn't authenticated
        if(currentUser == null)
        {
            startActivity( new Intent( UserActivity.this, Authentication.class ) );
        }
        else
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
                    Toast.makeText( UserActivity.this, "Welcome " + dataSnapshot.child( "name" ).getValue(), Toast.LENGTH_LONG ).show();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void loadDealsList()
    {
        adapter = new FirebaseRecyclerAdapter<TravelDeal, DealViewHolder>(
                TravelDeal.class, R.layout.travel_deal_layout, DealViewHolder.class, mDatabaseReference) {
            @Override
            protected void populateViewHolder(final DealViewHolder viewHolder, TravelDeal model, final int position) {
                viewHolder.deal_title.setText( model.getTitle() );
                viewHolder.deal_desc.setText( model.getDescription() );
                viewHolder.deal_price.setText("$"+ model.getPrice() );
                Picasso.with( getBaseContext() ).load( model.getImage() ).into( viewHolder.deal_image );

                viewHolder.itemView.setOnClickListener( new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent passIntent = new Intent( UserActivity.this, InsertActivity.class );
                        passIntent.putExtra("DealId", adapter.getRef(position).getKey());
                        startActivity( passIntent );

                    }
                } );
            }
        };
        adapter.notifyDataSetChanged();
        recycler_deals.setAdapter( adapter );
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate( R.menu.list_activity_menu, menu );
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        switch (item.getItemId()){
            case R.id.insert_menu:
                startActivity( new Intent( this, NewInsertActivity.class ) );
                return true;
            case R.id.log_out:
                mAuth.signOut();
                startActivity( new Intent( this, Authentication.class ) );
                return true;
            default:
                return super.onOptionsItemSelected( item );
        }
    }
}
