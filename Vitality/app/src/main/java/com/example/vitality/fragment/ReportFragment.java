package com.example.vitality.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

import com.example.vitality.R;
import com.example.vitality.databinding.ActivityDashboardBinding;
import com.example.vitality.databinding.ActivityReportBinding;
import com.example.vitality.databinding.ActivityWorkoutLogBinding;
import com.example.vitality.entity.Diet;
import com.example.vitality.viewmodel.DietViewModel;
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
import java.util.List;

public class ReportFragment extends Fragment{
    private ActivityReportBinding binding;
    private DietViewModel dietViewModel;
    private BarChart chart;
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
        //chart = view.findViewById(R.id.chart);
        //binding the chart
        //chart = binding.chart;

        // Get a reference to the DietViewModel
        dietViewModel = new ViewModelProvider(this).get(DietViewModel.class);

        // Observe all diet in the database using LiveData and update the UI when there is a change
        dietViewModel.getAllDiets().observe(getViewLifecycleOwner(), new Observer<List<Diet>>() {
            @Override
            public void onChanged(List<Diet> diets) {
                // Get the percentage of cake and beef diets

                int beefNum = 0;
                int cakeNum = 0;
                for (Diet diet : diets) {
                    beefNum += diet.getBeefNum();
                    cakeNum += diet.getCakeNum();
                }
                int total = cakeNum + beefNum;
//                float beefPercent = ((float) beefNum / total) * 100f;
//                float cakePercent = ((float) cakeNum / total) * 100f;

                // Create a list of BarEntry objects for the chart
                List<BarEntry> barEntries = new ArrayList<>();
                barEntries.add(new BarEntry(0,beefNum));
                barEntries.add(new BarEntry(1,cakeNum));

                // Create a BarDataSet object
                BarDataSet barDataSet = new BarDataSet(barEntries, "Steps");
                barDataSet.setColors(ColorTemplate.COLORFUL_COLORS);

                ArrayList<IBarDataSet> dataSets = new ArrayList<>();
                List<String> xAxisValues = new ArrayList<>(Arrays.asList("Beef", "Cake"));

                binding.barChart.getXAxis().setValueFormatter(new
                        com.github.mikephil.charting.formatter.IndexAxisValueFormatter(xAxisValues));
                BarData barData = new BarData(barDataSet);
                binding.barChart.setData(barData);


                barData.setBarWidth(1.0f);
                binding.barChart.setVisibility(View.VISIBLE);
                binding.barChart.animateY(4000);
//description will be displayed as "Description Label" if not provided
                Description description = new Description();
                description.setText("Daily Steps");
                binding.barChart.setDescription(description);
//refresh the chart
                binding.barChart.invalidate();


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
        chart = null;
    }
}
