package com.example.vitality.fragment;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.example.vitality.LoginActivity;
import com.example.vitality.R;
import com.example.vitality.databinding.HomeFragmentBinding;
import com.example.vitality.retrofit.Weather;
import com.example.vitality.viewmodel.SharedViewModel;
import com.example.vitality.weather.Root;
import com.example.vitality.weather.WeatherApiInterface;
import com.google.firebase.auth.FirebaseAuth;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class HomeFragment extends Fragment   {

    private SharedViewModel model;
    private HomeFragmentBinding addBinding;
    private TextView textView;

    private long lastApiRequestTime = 0;

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

        //Weather.fetchWeather(this); // start fetching weather data



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

//        double latitude;
//        double longitude;

        final double[] locationValues = new double[2];
        LocationManager locationManager;
        LocationListener locationListener;

        locationManager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);

        locationListener = new LocationListener() {

            @Override
            public void onLocationChanged(Location location) {
                locationValues[0] = location.getLatitude();
                locationValues[1] = location.getLongitude();

                // 获取当前时间
                long currentTime = System.currentTimeMillis();

                // 检查是否已经过了足够的时间
                if (currentTime - lastApiRequestTime > 3000) {  // 每隔3s发送一次请求
                    lastApiRequestTime = currentTime;  // 更新上次请求时间

                    // 在这里获取天气
                    Retrofit retrofit = new Retrofit.Builder()
                            .baseUrl("https://api.openweathermap.org/data/2.5/")
                            .addConverterFactory(GsonConverterFactory.create())
                            .build();

                    WeatherApiInterface weatherApiInterface = retrofit.create(WeatherApiInterface.class);

                    String appId = "8c641d73139b712b4ae02e7774f3391c"; // 使用的appid

                    Call<Root> call = weatherApiInterface.getWeather(locationValues[0], locationValues[1], appId);
                    call.enqueue(new Callback<Root>() {
                        @Override
                        public void onResponse(Call<Root> call, Response<Root> response) {
                            Root root = response.body();
                            System.out.println();
                            double temp = root.getMain().getTemp()-273.15;
                            //kelvin to celsius  celsius = kelvin - 273.15

                            double temp_min = root.getMain().getTemp()-273.15;
                            double temp_max = root.getMain().getTemp()-273.15;

                            String description = root.weather.get(0).description;
                            String name = root.name;

                            String weatherText = "name: " + name
                                    + "Temperature: " + (int) temp + "°C\n"
                                    + "Min Temperature: " + (int) temp_min + "°C\n"
                                    + "Max Temperature: " + (int) temp_max + "°C\n"
                                    + "Description: " + description;

                            if(addBinding != null) {  // check if the binding is not null
                                addBinding.tempTextView.setText(weatherText);
                            }
                        }

                        @Override
                        public void onFailure(Call<Root> call, Throwable t) {
                            System.out.println(t.getMessage());
                        }
                    });
                }
            }


            @Override
            public void onStatusChanged(String provider, int status, Bundle extras) {}

            @Override
            public void onProviderEnabled(String provider) {}

            @Override
            public void onProviderDisabled(String provider) {}
        };


        if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // Check permission
            ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
        } else {
            // Request location updates
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);
        }
        //location

        //退出登录
        Button signOutButton = view.findViewById(R.id.SignOut_button);
        signOutButton.setOnClickListener(v -> {
            FirebaseAuth.getInstance().signOut();
            //调用firebase自带的signOut（）方法推出登录

            startActivity(new Intent(getActivity(), LoginActivity.class));
            //跳转至Login 界面
        });


        Button facebookButton = view.findViewById(R.id.facebookButton);
        facebookButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                navController.navigate(R.id.nav_facebook_fragment);
            }
        });


        return view;
    }

    //update weather info area
//    @Override
//    public void onWeatherFetchCompleted(Weather weather) {
//        String weatherString = weather.getWeatherString(); // get formatted weather string
//        textView.setText(weatherString); // update text view
//    }
//
//    @Override
//    public void onWeatherFetchFailed() {
//        // Handle the failed weather fetch (e.g. show an error message, retry the fetch, etc.)
//    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        addBinding = null;
    }
}

