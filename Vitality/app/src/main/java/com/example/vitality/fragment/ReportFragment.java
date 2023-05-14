package com.example.vitality.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

import com.example.vitality.R;
import com.example.vitality.databinding.ActivityDashboardBinding;
import com.example.vitality.databinding.ActivityReportBinding;
import com.example.vitality.databinding.ActivityWorkoutLogBinding;
import com.example.vitality.entity.Diet;
import com.example.vitality.entity.Food;
import com.example.vitality.utils.DateTimeUtils;
import com.example.vitality.viewmodel.DietViewModel;
import com.example.vitality.viewmodel.FoodViewModel;
import com.example.vitality.viewmodel.SharedViewModel;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.github.mikephil.charting.formatter.PercentFormatter;
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.github.mikephil.charting.components.Description;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ReportFragment extends Fragment{
    private ActivityReportBinding  binding;
    private FoodViewModel foodViewModel;
    private PieChart pieChart;
    private BarChart barChart;

    private TextView testarea;
    final String CHART_URL = "";
    public ReportFragment(){}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the View for this fragment using the binding
        binding = ActivityReportBinding.inflate(inflater, container, false);
        View view = binding.getRoot();
        SharedViewModel model = new
                ViewModelProvider(requireActivity()).get(SharedViewModel.class);

        foodViewModel = new ViewModelProvider(this).get(FoodViewModel.class);

        //binding the chart to the view
        pieChart = binding.pieChart;
        barChart = binding.barChart;
        //tvSelTime = view.findViewById(R.id.tvSelTime);
        testarea = binding.testtext;
        // binding fromDate and toDate
        binding.fromDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Display the date and time picker dialog
                DateTimeUtils.dateTimeDialog(requireContext(), new DateTimeUtils.OnDateTimeListener() {
                    @Override
                    public void onDateTime(String dateTime) {
                        binding.fromDate.setText(dateTime);
                    }
                });
            }
        });

        binding.toDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Display the date and time picker dialog
                DateTimeUtils.dateTimeDialog(requireContext(), new DateTimeUtils.OnDateTimeListener() {
                    @Override
                    public void onDateTime(String dateTime) {
                        binding.toDate.setText(dateTime);
                    }
                });
            }
        });



        // create a new bar chart based on food data
        binding.generateBarChartButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                testarea.setVisibility(View.GONE);
                //hide pie chart and show bar chart
                pieChart.setVisibility(View.GONE);
                barChart.setVisibility(View.VISIBLE);

                // get the date from the user
                String FromDate = binding.fromDate.getText().toString();
                String ToDate = binding.toDate.getText().toString();

                // check if the user has entered the date and FromDate
                if(FromDate.trim().isEmpty() || ToDate.trim().isEmpty()) {
                    // Display an error message to the user
                    Toast.makeText(getContext(), "Please fill in all the fields", Toast.LENGTH_SHORT).show();
                    return;
                }
                // check the FromDate is before the ToDate
                if(FromDate.compareTo(ToDate) > 0) {
                    // Display an error message to the user
                    Toast.makeText(getContext(), "Please enter a valid date range", Toast.LENGTH_SHORT).show();
                    return;
                }

                foodViewModel.getFoodBetweenDates(FromDate,ToDate).observe(getViewLifecycleOwner(), new Observer<List<Food>>() {
                    @Override
                    public void onChanged(List<Food> foodList) {
                        // if food db is empty, display an error message to the user
//                        if (foodList == null || foodList.isEmpty()) {
//                            String message1 = "Food database is empty";
//                            Toast.makeText(getContext(), message1, Toast.LENGTH_SHORT).show();
//                            return;
//                        }

                        // if no food data is found, display an error message to the user
                        //foodList = foodViewModel.getFoodBetweenDates(FromDate,ToDate).getValue();
                        if (foodList == null || foodList.isEmpty()) {
                            String message1 = "No food data found between " + FromDate + " and " + ToDate;
                            Toast.makeText(getContext(), message1, Toast.LENGTH_SHORT).show();
                            return;
                        }

                        String allFood = "";
                        // Loop through each food and add their details to the string
                        for (Food temp : foodList) {
                            String foodName = "Food name: " + temp.foodName;
                            String foodCalories = "Food calories: " + temp.calories;
                            String foodDate = "Food date: " + temp.date;
                            String combinedMessage = foodName + "\n" + foodCalories + "\n" + foodDate;
                            String foodDetails = (combinedMessage);
                            allFood = allFood + System.getProperty("line.separator") + foodDetails;
                        }

                        //testarea to check if the food data is working
                        testarea.setVisibility(View.VISIBLE);
                        testarea.setText(allFood.toString());

                        // get the food data with names and calories use map
                        HashMap<String, Float> foodTotals = new HashMap<>();

                        for (Food food : foodList) {
                            String foodName = food.getFoodName();
                            float calories = food.getCalories();

                            if (foodTotals.containsKey(foodName)) {
                                foodTotals.put(foodName, foodTotals.get(foodName) + calories);
                            } else {
                                foodTotals.put(foodName, calories);
                            }
                        }

                        //testarea to check if the hashmap is working
                        //testarea.setText(foodTotals.toString());

                        // create an empty BarData object with the X-axis label as "Food Name" and the Y-axis label as "Total Calories"
                        BarData barData = new BarData(new BarDataSet(new ArrayList<>(), "Food Name"));
                        barChart.setData(barData);

                        // let the chart animate and display its legend
                        barChart.animateY(2000);
                        barChart.getLegend().setEnabled(true);
                        barData.setBarWidth(1.0f);
                        // set the layoutParams of the barChart view
                        barChart.setLayoutParams(new LinearLayout.LayoutParams(
                                LinearLayout.LayoutParams.MATCH_PARENT, 600));


                        // customize the X-axis labels
                        XAxis xAxis = barChart.getXAxis();
                        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
                        xAxis.setDrawGridLines(false);

                        // create an array of String to hold the food names
                        String[] foodNames = new String[foodTotals.size()];

                        // loop through each foodName and its corresponding total calories and add it to the entries list and food names array
                        List<BarEntry> entries = new ArrayList<>();
                        int index = 0;

                        for (Map.Entry<String, Float> entry : foodTotals.entrySet()) {
                            float totalCalories = entry.getValue();
                            String foodName = entry.getKey();
                            entries.add(new BarEntry(index++, totalCalories));
                            foodNames[index-1] = foodName;
                        }

                        // testarea to check if the entries are working
                        //testarea.setText(entries.toString());



                        // set the X-axis labels using the food names array
                        xAxis.setValueFormatter(new IndexAxisValueFormatter(foodNames));


                        BarDataSet barDataSet = new BarDataSet(entries, "Total Calories");
                        barDataSet.setColors(ColorTemplate.COLORFUL_COLORS);
                        BarData data = new BarData(barDataSet);
                        barChart.setData(data);
                    }
                });
            }
        });

        // create a new pie chart based on food data
        binding.generatePieChartButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                testarea.setVisibility(View.GONE);
                //hide bar chart and show pie chart
                barChart.setVisibility(View.GONE);
                pieChart.setVisibility(View.VISIBLE);

                // get the date from the user
                String FromDate = binding.fromDate.getText().toString();
                String ToDate = binding.toDate.getText().toString();

                // check if the user has entered the date and FromDate
                if(FromDate.trim().isEmpty() || ToDate.trim().isEmpty()) {
                    // Display an error message to the user
                    Toast.makeText(getContext(), "Please fill in all the fields", Toast.LENGTH_SHORT).show();
                    return;
                }
                // check the FromDate is before the ToDate
                if(FromDate.compareTo(ToDate) > 0) {
                    // Display an error message to the user
                    Toast.makeText(getContext(), "Please enter a valid date range", Toast.LENGTH_SHORT).show();
                    return;
                }

                foodViewModel.getFoodBetweenDates(FromDate,ToDate).observe(getViewLifecycleOwner(), new Observer<List<Food>>() {
                    @Override
                    public void onChanged(List<Food> foodList) {


                        // if no food data is found, display an error message to the user
                        //foodList = foodViewModel.getFoodBetweenDates(FromDate,ToDate).getValue();
                        if (foodList == null || foodList.isEmpty()) {
                            String message1 = "No food data found between " + FromDate + " and " + ToDate;
                            Toast.makeText(getContext(), message1, Toast.LENGTH_SHORT).show();
                            return;
                        }

                        String allFood = "";
                        // Loop through each food and add their details to the string
                        for (Food temp : foodList) {
                            String foodName = "Food name: " + temp.foodName;
                            String foodCalories = "Food calories: " + temp.calories;
                            String foodDate = "Food date: " + temp.date;
                            String combinedMessage = foodName + "\n" + foodCalories + "\n" + foodDate;
                            String foodDetails = (combinedMessage);
                            allFood = allFood + System.getProperty("line.separator") + foodDetails;
                        }

                        //testarea to check if the food data is working
                        testarea.setVisibility(View.VISIBLE);
                        testarea.setText(allFood.toString());


                        // get the food data with names and calories use map
                        HashMap<String, Integer> foodTotals = new HashMap<>();

                        for (Food food : foodList) {
                            String foodName = food.getFoodName();
                            // float calories to int calories
                            int calories = Math.round(food.getCalories());


                            if (foodTotals.containsKey(foodName)) {
                                foodTotals.put(foodName, foodTotals.get(foodName) + calories);
                            } else {
                                foodTotals.put(foodName, calories);
                            }
                        }

                        //testarea.setText(foodTotals.toString());



                        // let the chart animate and display its legend
                        pieChart.animateY(2000);
                        pieChart.getLegend().setEnabled(true);


                        ArrayList<PieEntry> pieEntries = new ArrayList<>();
                        String label = "type";

                        //input data and fit data into pie chart entry
                        for(String type: foodTotals.keySet()){
                            pieEntries.add(new PieEntry(foodTotals.get(type).floatValue(), type));
                        }

                        //collecting the entries with label name
                        PieDataSet pieDataSet = new PieDataSet(pieEntries,label);
                        //setting text size of the value
                        pieDataSet.setValueTextSize(12f);

                        pieDataSet.setColors(ColorTemplate.COLORFUL_COLORS);

                        //grouping the data set from entry to chart
                        PieData pieData = new PieData(pieDataSet);
                        //showing the value of the entries, default true if not set
                        pieData.setDrawValues(true);

                        pieChart.setData(pieData);

                    }
                });
            }
        });


        //return to main page
        binding.returnButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Navigation.findNavController(view).navigateUp(); // Navigate up to the previous destination
            }
        });

        return view;
    }



    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
        barChart = null;
        pieChart = null;
    }

}
