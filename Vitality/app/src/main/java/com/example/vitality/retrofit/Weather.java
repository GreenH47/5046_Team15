package com.example.vitality.retrofit;

import com.google.gson.Gson;
import com.google.gson.annotations.SerializedName;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLDecoder;
import java.util.List;

import com.google.gson.annotations.SerializedName;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;
import retrofit2.http.Query;


public class Weather {
    // Define our API base URL, API key, and latitude and longitude parameters
    private static final String API_BASE_URL = "https://api.weatherbit.io/v2.0/";
    private static final String API_KEY = "98843ec000cb4eeca13534381d625f4e";
    private static final String API_LATITUDE = "-37.8136";
    private static final String API_LONGITUDE = "144.9631";

    // Define our Weather data model with the required data fields
    private String city;
    private double temperature;

    // Define a constructor for creating Weather objects manually
    private Weather(String city, double temperature) {
        this.city = city;
        this.temperature = temperature;
    }

    // Define a method for fetching weather data from our API and notifying a callback with the result
    public static void fetchWeather(final OnWeatherFetchCompletedListener listener) {
        // Use Retrofit to create an implementation of our weather service interface
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(API_BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        WeatherService service = retrofit.create(WeatherService.class);

        // Call the "getCurrentWeather" endpoint with our API key and location parameters
        Call<WeatherResponse> call = service.getCurrentWeather(API_KEY, API_LATITUDE, API_LONGITUDE);

        // Handle the API response with a callback
        call.enqueue(new Callback<WeatherResponse>() {
            @Override
            public void onResponse(Call<WeatherResponse> call, Response<WeatherResponse> response) {
                if (response.isSuccessful()) {
                    // Extract the City and Temperature values from the API response
                    WeatherResponse weatherResponse = response.body();

                    String city = weatherResponse.getData().get(0).getCityName();
                    double temperature = weatherResponse.getData().get(0).getTemperature();

                    // Create a new Weather object with the extracted values and pass it to the callback
                    Weather weather = new Weather(city, temperature);
                    listener.onWeatherFetchCompleted(weather);
                } else {
                    // Handle unsuccessful API response
                    listener.onWeatherFetchFailed();
                }
            }

            @Override
            public void onFailure(Call<WeatherResponse> call, Throwable t) {
                // Handle API call failure
                listener.onWeatherFetchFailed();
            }
        });
    }

    // Define an interface that describes the weather API endpoints and required parameters
    public interface WeatherService {
        @GET("current")
        Call<WeatherResponse> getCurrentWeather(@Query("key") String apiKey, @Query("lat") String latitude, @Query("lon") String longitude);
    }

    // Define a nested class that represents the expected JSON response from the weather API
    private static class WeatherResponse {
        @SerializedName("data")
        private List<Data> data;

        public List<Data> getData() {
            return data;
        }

        private static class Data {
            @SerializedName("city_name")
            private String cityName;
            @SerializedName("temp")
            private double temperature;

            public String getCityName() {
                return cityName;
            }

            public double getTemperature() {
                return temperature;
            }
        }
    }

    // Define a callback interface for handling completed weather fetches
    public interface OnWeatherFetchCompletedListener {
        void onWeatherFetchCompleted(Weather weather);
        void onWeatherFetchFailed();
    }

    // Define getter methods for the City and Temperature fields
    public String getCity() {
        return city;
    }

    public double getTemperature() {
        return temperature;
    }

    // Define a method for generating a summary message string for the weather data
    public String getWeatherString() {
        String weatherString = String.format("Hello %s! It's %.1f degrees outside.", city, temperature);

        // Handle case where city or temperature data is missing
        if (weatherString.contains("null")) {
            return "Weather data is unavailable";
        } else {
            return weatherString;
        }
    }
}

