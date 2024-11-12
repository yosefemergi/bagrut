package com.example.real.API;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.real.Adapters.NewsAdapter;
import com.example.real.R;
import com.example.real.API.NewsApiService;
import com.example.real.API.RetrofitClient;
import com.example.real.models.NewsResponse;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class StatisticsFragment extends Fragment {

    private RecyclerView newsRecyclerView;
    private NewsAdapter newsAdapter;
    private final String apiKey = "d721c4d2dcd64fccb01ca6015025a54b";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_statistics, container, false);

        newsRecyclerView = view.findViewById(R.id.newsRecyclerView);
        newsRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        // נבצע את קריאת ה-API ונאתחל את המתאם במידת הצורך
        fetchFinancialNews();

        return view;
    }

    private void fetchFinancialNews() {
        Log.d("StatisticsFragment", "Fetching financial news...");

        NewsApiService apiService = RetrofitClient.getClient().create(NewsApiService.class);
        Call<NewsResponse> call = apiService.getFinancialNews("finance", apiKey);

        call.enqueue(new Callback<NewsResponse>() {
            @Override
            public void onResponse(Call<NewsResponse> call, Response<NewsResponse> response) {
                Log.d("StatisticsFragment", "API Response received");
                Log.d("StatisticsFragment", "Response Code: " + response.code());

                if (response.isSuccessful() && response.body() != null) {
                    int articleCount = response.body().articles != null ? response.body().articles.size() : 0;
                    Log.d("StatisticsFragment", "Number of articles received: " + articleCount);

                    if (articleCount > 0) {
                        // אתחול המתאם פעם אחת
                        if (newsAdapter == null) {
                            newsAdapter = new NewsAdapter(response.body().articles);
                            newsRecyclerView.setAdapter(newsAdapter);
                        } else {
                            newsAdapter.updateArticles(response.body().articles); // מתאם יעודכן עם רשימה חדשה
                        }
                    } else {
                        Toast.makeText(getContext(), "No news articles available", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Log.d("StatisticsFragment", "Response unsuccessful");
                    Toast.makeText(getContext(), "Failed to load news", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<NewsResponse> call, Throwable t) {
                Log.e("StatisticsFragment", "Error: " + t.getMessage());
                Toast.makeText(getContext(), "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}
