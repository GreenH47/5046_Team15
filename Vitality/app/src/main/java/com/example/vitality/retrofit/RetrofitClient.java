package com.example.vitality.retrofit;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitClient {
    private static Retrofit retrofit;
    private static final String BASE_URL = "https://www.googleapis.com/";

    // Private constructor to prevent instantiation of the class
    private RetrofitClient() {}

    public static RetrofitInterface getRetrofitService() {
        if (retrofit == null) {
            // Create a new Retrofit instance if one doesn't already exist
            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        // Return the RetrofitInterface service object
        return retrofit.create(RetrofitInterface.class);
    }
}
