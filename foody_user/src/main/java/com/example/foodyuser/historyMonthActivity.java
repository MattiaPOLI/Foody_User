package com.example.foodyuser;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.design.button.MaterialButton;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class historyMonthActivity extends Activity {

    private ImageButton back;
    private LinearLayout monthsLayout;
    private SharedPreferences shared;
    private Context context;

    Map<String, ArrayList<String>> calendar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history_month);

        back = findViewById(R.id.calendar_back);
        monthsLayout = findViewById(R.id.layout_calendar);
        shared = getSharedPreferences("myPreference", MODE_PRIVATE);
        calendar = new HashMap<>();
        context = this;

        init();
    }

    private void init(){

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        fetchMonths();

    }

    private void fetchMonths(){
        DatabaseReference database = FirebaseDatabase.getInstance().getReference();
        Query query = database.child("archive").child("user").child(shared.getString("id",""));
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot ds : dataSnapshot.getChildren()){
                    Log.d("DATAFETCH",""+ds.getKey());
                    Pattern pattern = Pattern.compile("[0-9]+-[0-9]+");
                    Matcher matcher = pattern.matcher(ds.getKey());
                    if(matcher.matches()){
                        String[] parts = ds.getKey().split("-");
                        String month = parts[0];
                        String year = parts[1];
                        if(!calendar.containsKey(year)){
                            ArrayList<String> newMonth = new ArrayList<>();
                            newMonth.add(month);
                            calendar.put(year, newMonth);
                        }else {
                            calendar.get(year).add(month);
                        }
                    }
                }

                if(calendar.keySet().size() == 0){
                    Log.d("PROVA","NO History");
                    //NO History
                    LayoutInflater inflater = LayoutInflater.from(context);
                    View v = inflater.inflate(R.layout.calendar_layout, monthsLayout, false);

                    TextView year = v.findViewById(R.id.calendar_year);
                    int thisYear = Calendar.getInstance().get(Calendar.YEAR);
                    year.setText(String.format("%d",thisYear));
                    ConstraintLayout jan = v.findViewById(R.id.calendar_jan);
                    ConstraintLayout feb = v.findViewById(R.id.calendar_feb);
                    ConstraintLayout mar = v.findViewById(R.id.calendar_mar);
                    ConstraintLayout apr = v.findViewById(R.id.calendar_apr);
                    ConstraintLayout may = v.findViewById(R.id.calendar_may);
                    ConstraintLayout jun = v.findViewById(R.id.calendar_jun);
                    ConstraintLayout jul = v.findViewById(R.id.calendar_jul);
                    ConstraintLayout aug = v.findViewById(R.id.calendar_aug);
                    ConstraintLayout sep = v.findViewById(R.id.calendar_sep);
                    ConstraintLayout oct = v.findViewById(R.id.calendar_oct);
                    ConstraintLayout nov = v.findViewById(R.id.calendar_nov);
                    ConstraintLayout dec = v.findViewById(R.id.calendar_dec);

                    jan.setOnClickListener(null);
                    jan.setBackgroundResource(R.drawable.calendar_disabled_background);
                    jan.setElevation(0);

                    feb.setOnClickListener(null);
                    feb.setBackgroundResource(R.drawable.calendar_disabled_background);
                    feb.setElevation(0);

                    mar.setOnClickListener(null);
                    mar.setBackgroundResource(R.drawable.calendar_disabled_background);
                    mar.setElevation(0);

                    apr.setOnClickListener(null);
                    apr.setBackgroundResource(R.drawable.calendar_disabled_background);
                    apr.setElevation(0);

                    may.setOnClickListener(null);
                    may.setBackgroundResource(R.drawable.calendar_disabled_background);
                    may.setElevation(0);

                    jun.setOnClickListener(null);
                    jun.setBackgroundResource(R.drawable.calendar_disabled_background);
                    jun.setElevation(0);

                    jul.setOnClickListener(null);
                    jul.setBackgroundResource(R.drawable.calendar_disabled_background);
                    jul.setElevation(0);

                    aug.setOnClickListener(null);
                    aug.setBackgroundResource(R.drawable.calendar_disabled_background);
                    aug.setElevation(0);

                    sep.setOnClickListener(null);
                    sep.setBackgroundResource(R.drawable.calendar_disabled_background);
                    sep.setElevation(0);

                    oct.setOnClickListener(null);
                    oct.setBackgroundResource(R.drawable.calendar_disabled_background);
                    oct.setElevation(0);

                    nov.setOnClickListener(null);
                    nov.setBackgroundResource(R.drawable.calendar_disabled_background);
                    nov.setElevation(0);

                    dec.setOnClickListener(null);
                    dec.setBackgroundResource(R.drawable.calendar_disabled_background);
                    dec.setElevation(0);

                    monthsLayout.addView(v);
                }else{
                    for(String s : calendar.keySet()){
                        LayoutInflater inflater = LayoutInflater.from(context);
                        View v = inflater.inflate(R.layout.calendar_layout, monthsLayout, false);

                        TextView year = v.findViewById(R.id.calendar_year);
                        year.setText(s);
                        ConstraintLayout jan = v.findViewById(R.id.calendar_jan);
                        ConstraintLayout feb = v.findViewById(R.id.calendar_feb);
                        ConstraintLayout mar = v.findViewById(R.id.calendar_mar);
                        ConstraintLayout apr = v.findViewById(R.id.calendar_apr);
                        ConstraintLayout may = v.findViewById(R.id.calendar_may);
                        ConstraintLayout jun = v.findViewById(R.id.calendar_jun);
                        ConstraintLayout jul = v.findViewById(R.id.calendar_jul);
                        ConstraintLayout aug = v.findViewById(R.id.calendar_aug);
                        ConstraintLayout sep = v.findViewById(R.id.calendar_sep);
                        ConstraintLayout oct = v.findViewById(R.id.calendar_oct);
                        ConstraintLayout nov = v.findViewById(R.id.calendar_nov);
                        ConstraintLayout dec = v.findViewById(R.id.calendar_dec);

                        if(calendar.get(s).contains("1")){
                            jan.setOnClickListener(null);
                            jan.setBackgroundResource(R.drawable.calendar_enabled_background);
                            jan.setElevation(getResources().getDimensionPixelSize(R.dimen.short10));
                        }else{
                            jan.setOnClickListener(null);
                            jan.setBackgroundResource(R.drawable.calendar_disabled_background);
                            jan.setElevation(0);
                        }



                        if(calendar.get(s).contains("2")){
                            feb.setOnClickListener(null);
                            feb.setBackgroundResource(R.drawable.calendar_enabled_background);
                            feb.setElevation(getResources().getDimensionPixelSize(R.dimen.short10));
                        }else{
                            feb.setOnClickListener(null);
                            feb.setBackgroundResource(R.drawable.calendar_disabled_background);
                            feb.setElevation(0);
                        }

                        if(calendar.get(s).contains("3")){
                            mar.setOnClickListener(null);
                            mar.setBackgroundResource(R.drawable.calendar_enabled_background);
                            mar.setElevation(getResources().getDimensionPixelSize(R.dimen.short10));
                        }else{
                            mar.setOnClickListener(null);
                            mar.setBackgroundResource(R.drawable.calendar_disabled_background);
                            mar.setElevation(0);
                        }


                        if(calendar.get(s).contains("4")){
                            apr.setOnClickListener(null);
                            apr.setBackgroundResource(R.drawable.calendar_enabled_background);
                            apr.setElevation(getResources().getDimensionPixelSize(R.dimen.short10));
                        }else{
                            apr.setOnClickListener(null);
                            apr.setBackgroundResource(R.drawable.calendar_disabled_background);
                            apr.setElevation(0);
                        }


                        if(calendar.get(s).contains("5")){
                            may.setOnClickListener(null);
                            may.setBackgroundResource(R.drawable.calendar_enabled_background);
                            may.setElevation(getResources().getDimensionPixelSize(R.dimen.short10));
                        }else{
                            may.setOnClickListener(null);
                            may.setBackgroundResource(R.drawable.calendar_disabled_background);
                            may.setElevation(0);
                        }


                        if(calendar.get(s).contains("6")){
                            jun.setOnClickListener(null);
                            jun.setBackgroundResource(R.drawable.calendar_enabled_background);
                            jun.setElevation(getResources().getDimensionPixelSize(R.dimen.short10));
                        }else{
                            jun.setOnClickListener(null);
                            jun.setBackgroundResource(R.drawable.calendar_disabled_background);
                            jun.setElevation(0);
                        }


                        if(calendar.get(s).contains("7")){
                            jul.setOnClickListener(null);
                            jul.setBackgroundResource(R.drawable.calendar_enabled_background);
                            jul.setElevation(getResources().getDimensionPixelSize(R.dimen.short10));
                        }else{
                            jul.setOnClickListener(null);
                            jul.setBackgroundResource(R.drawable.calendar_disabled_background);
                            jul.setElevation(0);
                        }

                        if(calendar.get(s).contains("8")){
                            aug.setOnClickListener(null);
                            aug.setBackgroundResource(R.drawable.calendar_enabled_background);
                            aug.setElevation(getResources().getDimensionPixelSize(R.dimen.short10));
                        }else{
                            aug.setOnClickListener(null);
                            aug.setBackgroundResource(R.drawable.calendar_disabled_background);
                            aug.setElevation(0);
                        }


                        if(calendar.get(s).contains("9")){
                            sep.setOnClickListener(null);
                            sep.setBackgroundResource(R.drawable.calendar_enabled_background);
                            sep.setElevation(getResources().getDimensionPixelSize(R.dimen.short10));
                        }else{
                            sep.setOnClickListener(null);
                            sep.setBackgroundResource(R.drawable.calendar_disabled_background);
                            sep.setElevation(0);
                        }


                        if(calendar.get(s).contains("10")){
                            oct.setOnClickListener(null);
                            oct.setBackgroundResource(R.drawable.calendar_enabled_background);
                            oct.setElevation(getResources().getDimensionPixelSize(R.dimen.short10));
                        }else{
                            oct.setOnClickListener(null);
                            oct.setBackgroundResource(R.drawable.calendar_disabled_background);
                            oct.setElevation(0);
                        }


                        if(calendar.get(s).contains("11")){
                            nov.setOnClickListener(null);
                            nov.setBackgroundResource(R.drawable.calendar_enabled_background);
                            nov.setElevation(getResources().getDimensionPixelSize(R.dimen.short10));
                        }else{
                            nov.setOnClickListener(null);
                            nov.setBackgroundResource(R.drawable.calendar_disabled_background);
                            nov.setElevation(0);
                        }


                        if(calendar.get(s).contains("12")){
                            dec.setOnClickListener(null);
                            dec.setBackgroundResource(R.drawable.calendar_enabled_background);
                            dec.setElevation(getResources().getDimensionPixelSize(R.dimen.short10));
                        }else{
                            dec.setOnClickListener(null);
                            dec.setBackgroundResource(R.drawable.calendar_disabled_background);
                            dec.setElevation(0);
                        }

                        monthsLayout.addView(v);


                    }
                }



                for(String s : calendar.keySet()){
                    Log.d("PROVA",""+s);
                    for(String b: calendar.get(s))
                        Log.d("PROVA","    "+b);
                }



            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


    }
}