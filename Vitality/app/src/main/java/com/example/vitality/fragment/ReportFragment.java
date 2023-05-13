package com.example.vitality.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
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

        //chart = view.findViewById(R.id.chart);
        //binding the chart
        //chart = binding.chart;

        // Get a reference to the foodViewModel
        //foodViewModel = new ViewModelProvider(requireActivity()).get(FoodViewModel.class);
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
                //dateDialog();
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



        // create a new pie chart based on food data
        binding.generateBarChartButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
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
                    public void onChanged(List<Food> foods) {
                        // if no food data is found, display an error message to the user
                        if (foods == null || foods.isEmpty()) {
                            String message1 = "No food data found between " + FromDate + " and " + ToDate;
                            final String message2 = "";
                            foodViewModel.getDataRange().observe(getViewLifecycleOwner(), new Observer<String>() {
                                @Override
                                public void onChanged(String dateRange) {
                                    String updatedMessage2 = message2 + dateRange;

                                    String message = message1 + "\nDate range: " + updatedMessage2;
                                    Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();
                                }
                            });
                            return;
                        }

                        String foodtest = "";
                        float totalCalories = 0;
                        // get the food data with names and calories use map
                        Map<String, Float> foodNameCaloriesMap = new HashMap<>();
                        for (Food food : foods) {
                            String foodName = food.getFoodName();
                            float calories = food.getCalories();
                            if (foodNameCaloriesMap.containsKey(foodName)) {
                                foodtest += foodName;
                                calories += foodNameCaloriesMap.get(foodName);
                            }
                            foodNameCaloriesMap.put(foodName, calories);
                        }

                        testarea.setText(foodtest);


                        List<BarEntry> entries = new ArrayList<>();
                        int i = 0;
                        for (Map.Entry<String, Float> entry : foodNameCaloriesMap.entrySet()) {
                            entries.add(new BarEntry(i++, entry.getValue(), entry.getKey()));
                        }
                        BarDataSet dataSet = new BarDataSet(entries, "Food Name and Calories");
                        BarData barData = new BarData(dataSet);
                        barChart.setData(barData);
                        barChart.invalidate();


                    }
                });

//




            }
        });

        binding.generatePieChartButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
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

                // get the food data from the database using getFoodBetweenDates
                LiveData<List<Food>> filteredFoods = foodViewModel.getFoodBetweenDates(FromDate, ToDate);

                // if no food data is found, display an error message to the user
                // if no food data is found, display an error message to the user
                if (filteredFoods.getValue().isEmpty()) {
                    String message1 = "No food data found between " + FromDate + " and " + ToDate;
                    final String message2 = "";
                    foodViewModel.getDataRange().observe(getViewLifecycleOwner(), new Observer<String>() {
                        @Override
                        public void onChanged(String dateRange) {
                            String updatedMessage2 = message2 + dateRange;

                            String message = message1 + "\nDate range: " + updatedMessage2;
                            Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();
                        }
                    });
                    return;
                }


                // Initialize the calorie totals map with empty values
                Map<String, Float> calorieTotals = new HashMap<>();
                calorieTotals.put("Rice", 0f);
                calorieTotals.put("Meat", 0f);
                calorieTotals.put("Fish", 0f);
                calorieTotals.put("Vegetable", 0f);
                calorieTotals.put("Cake", 0f);
                calorieTotals.put("Drinks", 0f);

                for (Food food : filteredFoods.getValue()) {
                    String foodName = food.getFoodName();
                    Float calorieTotal = calorieTotals.get(foodName);
                    calorieTotal += food.getCalories();
                    calorieTotals.put(foodName, calorieTotal);
                }


                // create a new bar chart based on the total calories of each kinds of foodName




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

//    // Display the date and time picker dialog
//    private void dateDialog() {
//        DateTimeUtils.dateTimeDialog(requireContext(), new DateTimeUtils.OnDateTimeListener() {
//            @Override
//            public void onDateTime(String dateTime) {
//                tvSelTime.setText(dateTime);
//            }
//        });
//    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
        barChart = null;
    }

}
