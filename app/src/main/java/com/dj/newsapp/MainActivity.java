package com.dj.newsapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.newsapp.R;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;
import com.google.android.gms.ads.interstitial.InterstitialAd;
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity implements CategoryAdapter.CategoryClickInterface{

    //Api Key = f920cdfddb4b4d71a871e1db35506312
    //https://newsapi.org/v2/top-headlines/sources?category=businessapiKey=API_KEY
    //https://newsapi.org/v2/top-headlines?country=in&apiKey=f920cdfddb4b4d71a871e1db35506312

    private RecyclerView newsRV, categoryRV;
    private ProgressBar loading;
    private ArrayList<Articles> articlesArrayList;
    private ArrayList<CategoryModel> categoryAdapterArrayList;
    private CategoryAdapter categoryAdapter;
    private NewsAdapter newsAdapter;
    private AdView mAdView;
    int position = 0;
    private InterstitialAd mInterstitialAd;
    private Button aboutUsBtn, queryBtn;
    private Spinner spinner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        newsRV = findViewById(R.id.News);
        categoryRV = findViewById(R.id.Categories);
        loading = findViewById(R.id.ProgressBar);
        aboutUsBtn = findViewById(R.id.aboutUs);
        queryBtn = findViewById(R.id.queruButton);

        articlesArrayList = new ArrayList<>();
        categoryAdapterArrayList = new ArrayList<CategoryModel>();


        newsAdapter = new NewsAdapter(articlesArrayList, this);
        categoryAdapter = new CategoryAdapter(categoryAdapterArrayList, this, this::onCategoryClick);
        newsRV.setLayoutManager(new LinearLayoutManager(this));
        newsRV.setAdapter(newsAdapter);
        categoryRV.setAdapter(categoryAdapter);
        getCategories();
        getNews("All");
        newsAdapter.notifyDataSetChanged();


        String category = categoryAdapterArrayList.get(position).getCategory();
        getNews(category);

        aboutUsBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent c = new Intent(MainActivity.this, AboutUS.class);
                startActivity(c);
            }
        });

        queryBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent query = new Intent(MainActivity.this, QueryActivity.class);
                startActivity(query);
            }
        });

        MobileAds.initialize(this, new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(InitializationStatus initializationStatus) {
            }
        });

        setAds();

        mAdView = findViewById(R.id.bannerAdView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);
    }


    private void setAds() {
        AdRequest adRequest = new AdRequest.Builder().build();

        InterstitialAd.load(this, "ca-app-pub-7515447854449753/1386707088", adRequest, new InterstitialAdLoadCallback() {
            @Override
            public void onAdLoaded(@NonNull InterstitialAd interstitialAd) {
                // The mInterstitialAd reference will be null until
                // an ad is loaded.
                mInterstitialAd = interstitialAd;

            }

            @Override
            public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
                // Handle the error
                mInterstitialAd = null;
            }
        });
    }

    private void getCategories(){
        categoryAdapterArrayList.add(new CategoryModel("ALL", "https://media.istockphoto.com/id/1434301869/photo/home-delivery-gone-right-all-smiles.jpg?b=1&s=170667a&w=0&k=20&c=0g6t42JBazQ7os8d3TJRxunqOFc2SZAvHDziyQFMGeo="));
        categoryAdapterArrayList.add(new CategoryModel("Technology", "https://images.unsplash.com/photo-1461749280684-dccba630e2f6?ixlib=rb-4.0.3&ixid=MnwxMjA3fDB8MHxzZWFyY2h8NHx8dGVjaG5vbG9naWVzfGVufDB8fDB8fA%3D%3D&auto=format&fit=crop&w=500&q=60"));
        categoryAdapterArrayList.add(new CategoryModel("Science", "https://media.istockphoto.com/id/1346675635/photo/modern-medical-research-laboratory-portrait-of-latin-and-black-young-scientists-using.jpg?b=1&s=170667a&w=0&k=20&c=UmopokGx9iVuio-7v0MpN-U0aBJVkKZ1K5dQI1MyoSs="));
        categoryAdapterArrayList.add(new CategoryModel("Sports", "https://media.istockphoto.com/id/1355086328/photo/american-football-championship-teams-ready-professional-players-aggressive-face-off-ready-for.jpg?b=1&s=170667a&w=0&k=20&c=oppcugL5if8KL9citBpZoSIlGL5bvzxxiKYOnA-WvRs="));
        categoryAdapterArrayList.add(new CategoryModel("Business", "https://media.istockphoto.com/id/1406199232/photo/economic-crisis-of-2022-has-affected-all-possible-spheres.jpg?b=1&s=170667a&w=0&k=20&c=O0cyJmCMxYXTzhRQkwDsGnrvEJ16GDC2Tv45bzYHnxY="));
        categoryAdapterArrayList.add(new CategoryModel("Entertainment", "https://media.istockphoto.com/id/507832501/photo/family-watching-television-in-living-room.jpg?b=1&s=170667a&w=0&k=20&c=iQS9Ku-59GLlmR85RISWJJKUIl9HSRHajgho-j7MP9g="));
        categoryAdapterArrayList.add(new CategoryModel("Health", "https://images.unsplash.com/photo-1506126613408-eca07ce68773?ixlib=rb-4.0.3&ixid=MnwxMjA3fDB8MHxzZWFyY2h8M3x8SGVhbHRofGVufDB8fDB8fA%3D%3D&auto=format&fit=crop&w=500&q=60"));

        categoryAdapter.notifyDataSetChanged();

    }

    private void getNews(String category){

        loading.setVisibility(View.VISIBLE);
        articlesArrayList.clear();
        String categoryURL = "https://newsapi.org/v2/top-headlines?country=in&category=" + category + "&apikey=f920cdfddb4b4d71a871e1db35506312";
        String url = "https://newsapi.org/v2/top-headlines?country=in&excludeDomains=stackoverflow.com&sortBy=publishedAt&language=en&apiKey=f920cdfddb4b4d71a871e1db35506312";
        String baseUrl = "https://newsapi.org/";
        Retrofit retrofit = new Retrofit.Builder().baseUrl(baseUrl).addConverterFactory(GsonConverterFactory.create()).build();
        RetrofitAPI retrofitAPI = retrofit.create(RetrofitAPI.class);
        Call<NewsModel> call;

        if(category.equals("ALL")){
            call = retrofitAPI.getAllNews(url);
        }
        else{
            call = retrofitAPI.getNewsByCategory(categoryURL);
        }

        call.enqueue(new Callback<NewsModel>() {
            @Override
            public void onResponse(Call<NewsModel> call, Response<NewsModel> response) {
                NewsModel newsModel = response.body();
                loading.setVisibility(View.GONE);

                if(response.body() != null){
                    ArrayList<Articles> articles = newsModel.getArticles();

                    for(int i=0; i<articles.size(); i++){
                        articlesArrayList.add(new Articles(articles.get(i).getTitle(), articles.get(i).getDescription(), articles.get(i).getUrlToImage(),articles.get(i).getUrl(), articles.get(i).getContent()));
                        newsAdapter.notifyDataSetChanged();
                    }
                }
            }


            @Override
            public void onFailure(Call<NewsModel> call, Throwable t) {
                Toast.makeText(MainActivity.this, "Fail to get News", Toast.LENGTH_SHORT).show();
            }
        });

    }

    @Override
    public void onCategoryClick(int position) {

        String category = categoryAdapterArrayList.get(position).getCategory();
        getNews(category);

    }

}