package com.example.vitality.weather;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

//public interface WeatherApiInterface {
//
//    //https://api.openweathermap.org/data/2.5/weather?lat=35.650711&lon=-78.456390&appid=714216b5d55e9f8932359accb29635a1
//
//    @GET("weather?lat=35.650711&lon=-78.456390&appid=714216b5d55e9f8932359accb29635a1")
//    //@GET("@string/clayton_weather")
//    //lat=35.650711&lon=-78.456390为clayton的经纬度
//    //根据经纬度来调用api，获得json格式的天气信息
//    Call<Root> getWeather();
//}

public interface WeatherApiInterface {

    @GET("weather")
    Call<Root> getWeather(
            @Query("lat") double latitude,
            @Query("lon") double longitude,
            @Query("appId") String appId
    );
}
