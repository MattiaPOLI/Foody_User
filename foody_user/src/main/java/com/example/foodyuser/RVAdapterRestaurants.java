package com.example.foodyuser;

import android.animation.Animator;
import android.app.Activity;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.transition.TransitionValues;
import android.util.Log;
import android.support.v4.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class RVAdapterRestaurants  extends RecyclerView.Adapter<RVAdapterRestaurants.CardViewHolder>{

    private ArrayList<Restaurant> restaurants;


    RVAdapterRestaurants(ArrayList<Restaurant> restaurants){
        this.restaurants = restaurants;
    }

    @NonNull
    @Override
    public CardViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.restaurant_card_display, viewGroup, false);

        return new CardViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull final CardViewHolder cardViewHolder, int i) {

        cardViewHolder.restaurantName.setText(restaurants.get(i).getName());
        cardViewHolder.restaurantDescription.setText(restaurants.get(i).getKitchensString());
        cardViewHolder.restaurantDeliveryPrice.setText(restaurants.get(i).getDeliveryPriceString());
        cardViewHolder.restaurantDistance.setText(restaurants.get(i).getDistanceString());

        cardViewHolder.card.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("TITLECHECK","Clicked");
                Intent intent = new Intent(v.getContext(), RestaurantShow.class);
                Pair<View, String> image = Pair.create((View)cardViewHolder.restaurantBackground, v.getContext().getString(R.string.transition_restaurant_card_image));
                Pair<View, String> shadow = Pair.create((View)cardViewHolder.restaurantShadow, v.getContext().getString(R.string.transition_restaurant_card_shadow));
                Pair<View, String> name = Pair.create((View)cardViewHolder.restaurantName, v.getContext().getString(R.string.transition_restaurant_card_name));


                ActivityOptionsCompat options =
                        ActivityOptionsCompat.makeSceneTransitionAnimation((Activity)v.getContext(),image, shadow, name);
                //Start the Intent
                ActivityCompat.startActivity(v.getContext(), intent, options.toBundle());
            }
        });

    }

    @Override
    public int getItemCount() {
        return restaurants.size();
    }

    static class CardViewHolder extends RecyclerView.ViewHolder {

        ImageView restaurantBackground;
        ImageView restaurantShadow;
        TextView restaurantName;
        TextView restaurantDescription;
        TextView restaurantDeliveryPrice;
        CardView card;
        TextView restaurantDistance;

        CardViewHolder(View itemView) {
            super(itemView);
            restaurantBackground = itemView.findViewById(R.id.restaurant_background);
            restaurantShadow = itemView.findViewById(R.id.restaurant_shadow);
            restaurantName = itemView.findViewById(R.id.restaurant_name);
            restaurantDescription = itemView.findViewById(R.id.restaurant_description);
            restaurantDeliveryPrice = itemView.findViewById(R.id.restaurant_delivery_price);
            restaurantDistance = itemView.findViewById(R.id.restaurant_distance);
            card = itemView.findViewById(R.id.cv);
        }
    }

    //TRIES
    public Animator createAnimator(ViewGroup sceneRoot, TransitionValues startValues, TransitionValues endValues){
        if(startValues == null || endValues == null)
            return null;



        return null;
    }
}
