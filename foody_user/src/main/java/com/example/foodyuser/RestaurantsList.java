package com.example.foodyuser;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class RestaurantsList extends AppCompatActivity {

    private EditText searchField;
    //private RecyclerView queryResult;
    private RecyclerView restaurantList;
    private ArrayList<Restaurant> restaurants = new ArrayList<>();
    private RVAdapterRestaurants adapter;
    private boolean add = true;
    private ImageButton back;
    private ImageButton filter;
    private AlertDialog foodChooseType;
    private final boolean[] checkedFoods = new boolean[27];
    private ArrayList<String> selectedFoods;
    private String[] foodCategories;
    private ArrayList<Integer> indexFoods;
    private boolean unchanged, checkString = true;
    private String dialogCode = "ok";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_restaurants_list);

        searchField = findViewById(R.id.search_field);
        //queryResult = findViewById(R.id.query_result);
        //queryResult.setHasFixedSize(true);
        searchField.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }
            @Override
            public void afterTextChanged(Editable editable) {
                filter(editable.toString());
            }
        });

        init();

    }

    private void init(){
        restaurantList = findViewById(R.id.restaurants_list);
        LinearLayoutManager llm = new LinearLayoutManager(this);
        restaurantList.setLayoutManager(llm);
        filter = findViewById(R.id.filterButton);
        back = findViewById(R.id.backButton);

        DatabaseReference database = FirebaseDatabase.getInstance().getReference();
        DatabaseReference ref = database.child("restaurantsInfo");
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    add = true;
                    for (Restaurant rest : restaurants){
                        if (ds.getKey().compareTo(rest.getName()) == 0) {
                            add = false;
                            break;
                        }
                    }
                    if (add) {
                        for (DataSnapshot ds1 : ds.getChildren()) {
                            Restaurant restaurant = ds1.getValue(Restaurant.class);
                            restaurants.add(restaurant);
                        }
                    }
                }
                adapter = new RVAdapterRestaurants(restaurants);
                restaurantList.setAdapter(adapter);
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.d("SWSW", databaseError.getMessage());
            }
        });

        selectedFoods = new ArrayList<>();
        indexFoods = new ArrayList<>();
        foodCategories = getResources().getStringArray(R.array.foodcategory_array);

        filter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showPickFood(filter);
            }
        });
    }


    private void filter(String text) {
        ArrayList<Restaurant> filteredNames = new ArrayList<>();

        for (int i = 0; i < restaurants.size(); i++)
            if(restaurants.get(i).getName().toLowerCase().contains(text.toLowerCase()))
                filteredNames.add(restaurants.get(i));

        adapter.filterList(filteredNames);
    }

    private void filterCuisine(ArrayList<String> text) {
        ArrayList<Restaurant> filteredNames = new ArrayList<>();

        if(text.size() == 0) {
            adapter.filterList(restaurants);
            return;
        }

        for(int i = 0; i < restaurants.size(); i++) {
            ArrayList<String> cuisines = restaurants.get(i).getCuisines();
            for(String c : cuisines) {
                for (String s : text) {
                    if (c.toLowerCase().contains(s.toLowerCase())) {
                        if(!filteredNames.contains(restaurants.get(i))) {
                            filteredNames.add(restaurants.get(i));
                        }
                    }
                }
            }
        }

        adapter.filterList(filteredNames);
    }

    private void populateCheckedFoods() {
        for(int i = 0; i < 27; i++)
            checkedFoods[i] = false;

        int index = indexFoods.size();

        for(int i = 0; i < index; i++) {
            checkedFoods[indexFoods.get(i)] = true;
        }
    }

    public void showPickFood(View view) {
        populateCheckedFoods();

        final AlertDialog.Builder builder = new AlertDialog.Builder(this,  R.style.AppCompatAlertDialogStyle);

        builder.setMultiChoiceItems(foodCategories, checkedFoods, new DialogInterface.OnMultiChoiceClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which, boolean isChecked) {
                if (isChecked) {
                    selectedFoods.add(String.valueOf(foodCategories[which]));
                    indexFoods.add(which);
                    checkedFoods[which] = true;
                } else {
                    selectedFoods.remove(String.valueOf(foodCategories[which]));
                    if(indexFoods.contains(which))
                        indexFoods.remove(Integer.valueOf(which));
                    checkedFoods[which] = false;
                }
            }
        });

        builder.setPositiveButton(R.string.accept, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                unchanged = false;

                filterCuisine(selectedFoods);
                dialogCode = "ok";
            }
        });

        builder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialogCode = "ok";
                dialog.dismiss();
            }
        });
        builder.setTitle(R.string.dialog_cuisine);
        foodChooseType = builder.create();
        dialogCode = "foodDialog";
        foodChooseType.show();
    }
}
