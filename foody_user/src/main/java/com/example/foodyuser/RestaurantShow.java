package com.example.foodyuser;

import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;
import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;

public class RestaurantShow extends AppCompatActivity {

    TextView title, cuisines, deliveryPrice, distance;
    ImageView image;
    Restaurant thisRestaurant;
    ArrayList<Card> cards;
    RecyclerView menu;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_restaurant_show);

        if(!init())
            finish();
    }

    private boolean init(){

        title = findViewById(R.id.restaurant_title);
        cuisines = findViewById(R.id.restaurant_cuisines);
        deliveryPrice = findViewById(R.id.restaurant_del_price);
        distance = findViewById(R.id.restaurant_dist);
        image = findViewById(R.id.restaurant_image);
        menu = findViewById(R.id.menu);
        image.setImageResource(R.drawable.user_static_background);
        Bundle extras = getIntent().getExtras();
        title.setText(extras.getString("restaurant_name",""));
        fetchRestaurant();
        fetchMenu();
        LinearLayoutManager llm = new LinearLayoutManager(this);
        menu.setLayoutManager(llm);
        return true;
    }

    private void fetchRestaurant(){

        if(thisRestaurant != null)
            return;

        DatabaseReference database = FirebaseDatabase.getInstance().getReference();
        DatabaseReference ref = database.child("restaurantsInfo").child(title.getText().toString());
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    thisRestaurant = ds.getValue(Restaurant.class);
                    cuisines.setText(thisRestaurant.getKitchensString());
                    deliveryPrice.setText(thisRestaurant.getDeliveryPriceString());
                    distance.setText(thisRestaurant.getDistanceString());
                    StorageReference mStorageRef = FirebaseStorage.getInstance().getReference();
                    mStorageRef.child("images/"+title.getText().toString()+"_profile.jpeg").getDownloadUrl()
                            .addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {
                                    Glide
                                            .with(getApplicationContext())
                                            .load(uri)
                                            .into(image);
                                }
                            });
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.d("SWSW", databaseError.getMessage());
            }
        });
    }

    private void fetchMenu(){
        DatabaseReference database = FirebaseDatabase.getInstance().getReference();
        DatabaseReference ref = database.child("restaurantsMenu").child(title.getText().toString());
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                cards = new ArrayList<>();
                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    for (DataSnapshot ds1 : ds.getChildren()) {
                        Card card = ds1.getValue(Card.class);
                        cards.add(card);
                    }
                }
                RVAdapterMenu adapterMenu = new RVAdapterMenu(cards);
                menu.setAdapter(adapterMenu);
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.d("SWSW", databaseError.getMessage());
            }
        });
    }
}