package com.example.foodyrestaurant;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.button.MaterialButton;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class HistoryFragment extends Fragment {

    private HashMap<String, Integer> dishes = new HashMap<>();
    private TextView firstDish, firstDishNumber;
    private TextView secondDish, secondDishNumber;
    private TextView thirdDish, thirdDishNumber;
    private TextView totalIncome;
    private BarChart barChart;
    private PieChart pieChart;
    private HashMap<Integer, Integer> frequency = new HashMap<>();
    private Float amount;
    private Integer accepted, rejected;
    private List<Map.Entry<String, Integer>> top3;
    private MaterialButton button;

    public HistoryFragment() {
        // Required empty public constructor
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_history, container, false);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        firstDish = view.findViewById(R.id.text_first);
        firstDishNumber = view.findViewById(R.id.text_first_secondary);
        secondDish = view.findViewById(R.id.text_second);
        secondDishNumber = view.findViewById(R.id.text_second_secondary);
        thirdDish = view.findViewById(R.id.text_third);
        thirdDishNumber = view.findViewById(R.id.text_third_secondary);
        totalIncome = view.findViewById(R.id.income);
        barChart = view.findViewById(R.id.barChart);
        pieChart = view.findViewById(R.id.pieChart);
        button = view.findViewById(R.id.enter_order_history);
        accepted = 0;
        rejected = 0;

        for(int i = 0; i < 24; i++){
            frequency.put(i, 0);
        }

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), HistoryPickMonth.class);
                startActivity(intent);
            }
        });

        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();

        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("archive")
                .child("restaurant").child(firebaseUser.getUid());

        DatabaseReference databaseFrequency = databaseReference.child("frequency");

        DatabaseReference databaseDishes = databaseReference.child("dishesCount");

        databaseDishes.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    for (DataSnapshot ds : dataSnapshot.getChildren()) {
                        dishes.put(ds.getKey(), ds.getValue(Integer.class));
                    }

                    if(dishes.size() > 2) {
                        List<Map.Entry<String, Integer>> entryList = new ArrayList<>(dishes.entrySet());
                        Collections.sort(entryList, new Comparator<Map.Entry<String, Integer>>() {
                            @Override
                            public int compare(Map.Entry<String, Integer> e1, Map.Entry<String, Integer> e2) {
                                return e2.getValue() - e1.getValue(); // descending order
                            }
                        });

                        // now let's get the top 3
                        top3 = new ArrayList<>(3);
                        for (Map.Entry<String, Integer> e : entryList) {
                            top3.add(e);
                            if (top3.size() == 3) {
                                break;
                            }
                        }

                        Log.d("MADMAD", "1 : " + top3.get(0));
                        Log.d("MADMAD", "2 : " + top3.get(1));
                        Log.d("MADMAD", "3 : " + top3.get(2));

                        firstDish.setText(top3.get(0).getKey());
                        secondDish.setText(top3.get(1).getKey());
                        thirdDish.setText(top3.get(2).getKey());
                        firstDishNumber.setText(top3.get(0).getValue()+ " " + getResources().getString(R.string.text_orders));
                        secondDishNumber.setText(top3.get(1).getValue()+ " " + getResources().getString(R.string.text_orders));
                        thirdDishNumber.setText(top3.get(2).getValue()+ " " + getResources().getString(R.string.text_orders));
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        databaseFrequency.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    for (DataSnapshot ds : dataSnapshot.getChildren()) {
                        frequency.put(Integer.parseInt(ds.getKey()), ds.getValue(Integer.class));
                    }
                } else {
                    for (int i = 0; i < 24; i ++){
                        frequency.put(i, 0);
                    }
                }

                drawChart();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot ds : dataSnapshot.getChildren()) {
                    if(ds.getKey().compareTo("amount") == 0) {
                        amount = ds.getValue(Float.class);
                    }
                    if(ds.getKey().compareTo("accepted") == 0) {
                        accepted = ds.getValue(Integer.class);
                    }
                    if(ds.getKey().compareTo("rejected") == 0) {
                        rejected = ds.getValue(Integer.class);
                    }
                }
                totalIncome.setText(String.format(Locale.getDefault(), "%.2f \u20ac", amount));
                drawPieCharts();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });

    }

    public void drawChart() {
        barChart.setDrawBarShadow(false);
        barChart.getDescription().setEnabled(false);
        barChart.setMaxVisibleValueCount(100);
        barChart.setPinchZoom(false);
        barChart.setDrawGridBackground(false);
        barChart.setDrawValueAboveBar(true);

        XAxis xl = barChart.getXAxis();
        xl.setGranularity(1f);
        xl.setPosition(XAxis.XAxisPosition.BOTTOM);
        xl.setAxisMinimum(0f);
        xl.setAxisMaximum(24f);
        xl.setLabelCount(9, true);
        xl.setDrawGridLines(false);

        YAxis leftAxis = barChart.getAxisLeft();
        leftAxis.setAxisMinimum(0f);
        leftAxis.setEnabled(false);
        leftAxis.setDrawLabels(true);
        leftAxis.setDrawGridLines(true);
        barChart.getAxisRight().setEnabled(false);

        final List<BarEntry> yVals1 = new ArrayList<>();

        Iterator it = frequency.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry<Integer, Integer> pair = (Map.Entry) it.next();
            if (pair.getValue() != 0)
                yVals1.add(new BarEntry(pair.getKey(), pair.getValue()));
        }

        final BarDataSet set = new BarDataSet(yVals1, "BarDataSet");
        set.setColor((Color.rgb(132, 171, 241)));

        final BarData data = new BarData(set);
        data.setDrawValues(false);
        data.setBarWidth(0.9f);
        barChart.setData(data);
        barChart.getLegend().setEnabled(false);
        barChart.setFitBars(true);
        barChart.setNoDataText("NO FREQUENCY IN ARCHIVE RIGHT NOW");
        barChart.animateY(3000);

    }

    public void drawPieCharts() {
        pieChart.setHighlightPerTapEnabled(true);
        pieChart.getDescription().setEnabled(false);

        final ArrayList<PieEntry> entries = new ArrayList<>();
        entries.add(new PieEntry(accepted, getResources().getString(R.string.text_accepted)));
        entries.add(new PieEntry(rejected, getResources().getString(R.string.text_rejected)));

        PieDataSet dataSet = new PieDataSet(entries, "Orders Results");
        int[] colors = {getResources().getColor(R.color.accept, getActivity().getTheme()),
                getResources().getColor(R.color.errorColor, getActivity().getTheme())};
        dataSet.setColors(colors);

        pieChart.setDrawEntryLabels(false);

        PieData data = new PieData(dataSet);
        pieChart.setData(data);

        dataSet.setSliceSpace(5f);
        dataSet.setSelectionShift(5f);

        dataSet.setDrawValues(false);
        pieChart.getLegend().setEnabled(false);

        int total = accepted+rejected;
        pieChart.setCenterText(total + "\n" + getResources().getString(R.string.text_orders));
        pieChart.setCenterTextSize(22f);
        pieChart.setNoDataText("NO ORDERS IN ARCHIVE RIGHT NOW");
        pieChart.animateXY(3000, 3000);

        final int totToText = total;
        pieChart.setOnChartValueSelectedListener(new OnChartValueSelectedListener() {
            @Override
            public void onValueSelected(Entry e, Highlight h) {
                if(entries.get(0).equals(e)) {
                    pieChart.setCenterText(accepted + "\n" + getResources().getString(R.string.text_accepted));
                } else {
                    pieChart.setCenterText(rejected + "\n" + getResources().getString(R.string.text_rejected));
                }
            }

            @Override
            public void onNothingSelected() {
                pieChart.setCenterText(totToText + "\n" + getResources().getString(R.string.text_orders));
            }
        });
    }
}
