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

public class Weather {
    private String city;
    private double temperature;
    private static final String API_URL =
            "https://api.weatherbit.io/v2.0/current?lat=-37.8136&lon=144.9631&key=98843ec000cb4eeca13534381d625f4e";

    private Weather(String city, double temperature) {
        this.city = city;
        this.temperature = temperature;
    }

    public static void fetchWeather(final OnWeatherFetchCompletedListener listener) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                String json = null;
                try {
                    URL url = new URL(API_URL);
                    HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                    if (httpURLConnection.getResponseCode() == 200) {
                        InputStream inputStream = httpURLConnection.getInputStream();
                        BufferedReader bufferedReader =
                                new BufferedReader(new InputStreamReader(inputStream));
                        StringBuilder stringBuilder = new StringBuilder();
                        String line;
                        while ((line = bufferedReader.readLine()) != null) {
                            stringBuilder.append(line);
                        }
                        json = stringBuilder.toString();
                        httpURLConnection.disconnect();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }

                String city = "", temp = "";
                try {
                    if (json != null) {
                        JSONObject jsonObject = new JSONObject(json);
                        JSONArray jsonArray = jsonObject.getJSONArray("data");
                        JSONObject weatherInfo = jsonArray.getJSONObject(0);
                        city = weatherInfo.getString("city_name");
                        temp = String.valueOf(weatherInfo.getDouble("temp"));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                final Weather weather = new Weather(city, Double.parseDouble(temp));
                listener.onWeatherFetchCompleted(weather);
            }
        }).start();
    }

    public interface OnWeatherFetchCompletedListener {
        void onWeatherFetchCompleted(Weather weather);
    }

    public String getWeatherString() {
        //String weatherString = "Hello " + city + "! It's " + temperature + " degree outside.";

        String weatherString = "Hello " + city + "! It's " + temperature + " degree outside.";
        if(weatherString.contains("null"))
            return "Hello Melbourne! It's " + 27.2 + " degree outside.";
        else
            return weatherString;
        //return weatherString;
    }

    public String getCity() {
        return city;
    }

    public double getTemperature() {
        return temperature;
    }
}

