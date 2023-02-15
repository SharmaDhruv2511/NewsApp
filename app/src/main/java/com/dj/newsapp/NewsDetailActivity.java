package com.dj.newsapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.newsapp.R;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.FullScreenContentCallback;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;
import com.google.android.gms.ads.interstitial.InterstitialAd;
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback;
import com.squareup.picasso.Picasso;

public class NewsDetailActivity extends AppCompatActivity {

    //interstial ad app id = ca-app-pub-7515447854449753~1741603335 and adunitid = ca-app-pub-7515447854449753/9382459633

    String title, description, content, imageURL, url;
    private TextView titleNews, subNews, contentOfNews;
    private ImageView newsImg;
    private Button newsButton;
    private AdView detailAdView;
    private InterstitialAd mInterstitialAd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news_detail);

        title = getIntent().getStringExtra("title");
        description = getIntent().getStringExtra("description");
        content = getIntent().getStringExtra("content");
        imageURL = getIntent().getStringExtra("image");
        url = getIntent().getStringExtra("url");

        titleNews = findViewById(R.id.detailTitle);
        subNews = findViewById(R.id.detaiSubtitle);
        contentOfNews = findViewById(R.id.newsContent);
        newsImg = findViewById(R.id.detailImg);
        newsButton = findViewById(R.id.NewsButton);

        titleNews.setText(title);
        subNews.setText(description);
        contentOfNews.setText(content);
        Picasso.get().load(imageURL).into(newsImg);


        MobileAds.initialize(this, new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(InitializationStatus initializationStatus) {
            }
        });

        detailAdView = findViewById(R.id.adViewdetail);
        AdRequest adRequest = new AdRequest.Builder().build();
        detailAdView.loadAd(adRequest);

        setAds();

        newsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse(url));
                startActivity(i);

                if (mInterstitialAd != null) {

                    mInterstitialAd.show(NewsDetailActivity.this);

                    mInterstitialAd.setFullScreenContentCallback(new FullScreenContentCallback() {
                        @Override
                        public void onAdDismissedFullScreenContent() {
                            super.onAdDismissedFullScreenContent();
                          //  startActivity(new Intent(MainActivity.this, InterstitialActivity.class));
                            mInterstitialAd = null;
                        }
                    });
                } else {

                    //startActivity(new Intent(MainActivity.this, InterstitialActivity.class));
                }
            }
        });
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
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


}