package com.example.vitality.retrofit;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface RetrofitInterface {
    // Define retrofit call method for custom search
    @GET("customsearch/v1")
    Call<SearchResponse> customSearch(
            @Query("key") String API_KEY,
            @Query("cx") String SEARCH_ID_cx,
            @Query("q") String keyword
    );
}
