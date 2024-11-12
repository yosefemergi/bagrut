package com.example.real.API;

import com.example.real.models.NewsResponse;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface NewsApiService {
    @GET("v2/everything")
    Call<NewsResponse> getFinancialNews(
            @Query("q") String query,
            @Query("apikey") String apiKey
    );
}
