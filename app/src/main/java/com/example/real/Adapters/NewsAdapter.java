package com.example.real.Adapters;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.real.R;
import com.example.real.models.NewsResponse;
import com.squareup.picasso.Picasso;

import java.util.List;

public class NewsAdapter extends RecyclerView.Adapter<NewsAdapter.NewsViewHolder> {

    private List<NewsResponse.Article> articles;

    public NewsAdapter(List<NewsResponse.Article> articles) {
        this.articles = articles;
    }

    public void updateArticles(List<NewsResponse.Article> newArticles) {
        this.articles = newArticles;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public NewsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_news, parent, false);
        return new NewsViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull NewsViewHolder holder, int position) {
        NewsResponse.Article article = articles.get(position);

        Log.d("NewsAdapter", "Binding article: " + article.title);
        Log.d("NewsAdapter", "Image URL: " + article.urlToImage);

        holder.titleTextView.setText(article.title);
        holder.descriptionTextView.setText(article.description);
        if (article.urlToImage != null && !article.urlToImage.isEmpty()) {
            Picasso.get().load(article.urlToImage).into(holder.imageView);
        } else {
            holder.imageView.setImageResource(R.drawable.placeholder_image);
        }

        holder.itemView.setOnClickListener(v -> {
            Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(article.url));
            holder.itemView.getContext().startActivity(browserIntent);
        });
    }

    @Override
    public int getItemCount() {
        return articles.size();
    }

    static class NewsViewHolder extends RecyclerView.ViewHolder {
        TextView titleTextView, descriptionTextView;
        ImageView imageView;

        NewsViewHolder(View itemView) {
            super(itemView);
            titleTextView = itemView.findViewById(R.id.titleTextView);
            descriptionTextView = itemView.findViewById(R.id.descriptionTextView);
            imageView = itemView.findViewById(R.id.imageView);
        }
    }
}
