package com.example.ayomide.travelmantics.ViewHolder;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.ayomide.travelmantics.R;

public class DealViewHolder extends RecyclerView.ViewHolder{

    public TextView deal_title, deal_desc, deal_price;
    public ImageView deal_image;

    public DealViewHolder(@NonNull View itemView) {
        super( itemView );

        deal_title = itemView.findViewById( R.id.travel_deal_title );
        deal_desc = itemView.findViewById( R.id.travel_deal_description );
        deal_price = itemView.findViewById( R.id.travel_deal_price );
        deal_image = itemView.findViewById( R.id.travel_deal_image );
    }
}
