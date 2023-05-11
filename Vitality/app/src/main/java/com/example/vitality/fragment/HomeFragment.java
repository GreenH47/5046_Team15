package com.example.vitality.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.example.vitality.R;
import com.example.vitality.databinding.HomeFragmentBinding;
import com.example.vitality.retrofit.Weather;
import com.example.vitality.viewmodel.SharedViewModel;

public class HomeFragment extends Fragment  implements Weather.OnWeatherFetchCompletedListener {

    private SharedViewModel model;
    private HomeFragmentBinding addBinding;
    private TextView textView;

    public HomeFragment(){}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the View for this fragment
        addBinding = HomeFragmentBinding.inflate(inflater, container, false);
        View view = addBinding.getRoot();

        // Get the NavController
        NavController navController = Navigation.findNavController(requireActivity(), R.id.nav_host_fragment);

        //add weather info
//        TextView textView = view.findViewById(R.id.text_message);
//        String weatherString = Weather.getWeatherString();
//        textView.setText(weatherString);
        textView = view.findViewById(R.id.text_message);

        Weather.fetchWeather(this); // start fetching weather data



        // Add a click listener to the dietLogButton to navigate to the DietlogFragment
        Button dietLogButton = view.findViewById(R.id.dietLogButton);
        dietLogButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                navController.navigate(R.id.action_homeFragment_to_dietlogFragment);
            }
        });

        Button foodButton = view.findViewById(R.id.foodButton);
        foodButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                navController.navigate(R.id.action_homeFragment_to_foodFragment);
            }
        });
        Button exerciseButton = view.findViewById(R.id.exerciseButton);
        exerciseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                navController.navigate(R.id.action_homeFragment_to_exerciseFragment);
            }
        });

        // Add a click listener to the dashboardButton to navigate to the DashboardFragment
        Button dashboardButton = view.findViewById(R.id.dashboardButton);
        dashboardButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                navController.navigate(R.id.action_homeFragment_to_dashboardFragment);
            }
        });

        // Add a click listener to the workoutLogButton to navigate to the workoutlogFragment
        Button workoutLogButton = view.findViewById(R.id.workoutLogButton);
        workoutLogButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                navController.navigate(R.id.action_homeFragment_to_workoutlogFragment);
            }
        });

        // Add a click listener to the reportButton to navigate to the reportFragment
        Button reportButton = view.findViewById(R.id.reportButton);
        reportButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                navController.navigate(R.id.action_homeFragment_to_reportFragment);
            }
        });

        // Add a click listener to the mapButton to navigate to the mapFragment
        Button mapButton = view.findViewById(R.id.mapButton);
        mapButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                navController.navigate(R.id.action_homeFragment_to_mapFragment);
            }
        });

        // Add a click listener to the retrofitButton to navigate to the retrofitFragment
        Button retrofitButton = view.findViewById(R.id.retrofitButton);
        retrofitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                navController.navigate(R.id.action_homeFragment_to_retrofitFragment);
            }
        });


        return view;
    }

    //update weather info area
    @Override
    public void onWeatherFetchCompleted(Weather weather) {
        String weatherString = weather.getWeatherString(); // get formatted weather string
        textView.setText(weatherString); // update text view
    }

    @Override
    public void onWeatherFetchFailed() {
        // Handle the failed weather fetch (e.g. show an error message, retry the fetch, etc.)
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        addBinding = null;
    }
}

