package com.aekan.navya.lpgbooking;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ExpandableListAdapter;
import android.widget.ExpandableListView;

import com.aekan.navya.lpgbooking.utilities.LPG_LinkExpandableAdapter;
import com.aekan.navya.lpgbooking.utilities.LPG_Utility;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;
import com.google.firebase.analytics.FirebaseAnalytics;

import static com.aekan.navya.lpgbooking.utilities.LPG_Utility.ifWeCanShowInterstitialAdNow;

/**
 * Created by arunramamurthy on 26/07/17.
 */

public class FAQs extends AppCompatActivity {

    private InterstitialAd mInterstitialAd;
    private FirebaseAnalytics mFireBaseAnalytics;

    @Override
    protected void onCreate(Bundle savedInstance) {
        super.onCreate(savedInstance);

        // inflate view
        setContentView(R.layout.faqs);

        //instantiate firebase analytics;
        mFireBaseAnalytics = FirebaseAnalytics.getInstance(getApplicationContext());

        //Set tool bar for the activity
        Toolbar toolbar = (Toolbar) findViewById(R.id.faqs_toolbar);
        setSupportActionBar(toolbar);
        //set navigation activities
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back_black_24dp);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // show interstitial ad if its time
                //or move back to Main Activity

                if (!showInterStitialAd()) {
                    Intent mainActivity = new Intent(getApplicationContext(), MainActivity.class);
                    startActivity(mainActivity);

                }


            }
        });
        toolbar.setTitle("FAQs");


        //create expandable list adapter
        ExpandableListAdapter adapterFAQs = new LPG_LinkExpandableAdapter(FAQs.this,
                LPG_Utility.getExpandableGroupData(getApplicationContext()),
                R.layout.faq_question,
                new String[]{LPG_Utility.EXPN_LIST_QUESTION},
                new int[]{R.id.faq_question},
                LPG_Utility.getExpandableChildData(getApplicationContext()),
                R.layout.faq_answer,
                new String[]{LPG_Utility.EXPN_LIST_ANSWER},
                new int[]{R.id.faq_answer},
                LPG_Utility.getFAQsSpanCollection(FAQs.this)
        );
        //set Adpater

        ExpandableListView faqSection = (ExpandableListView) findViewById(R.id.list);
        faqSection.setAdapter(adapterFAQs);

        //Banner Ad
        showBannerAd();

        //load interstitial ad
        loadInterstitialAd();
    }

    private void showBannerAd(){
        AdView adViewBanner = (AdView) findViewById(R.id.banner_faqs);
        AdRequest adRequest = new AdRequest.Builder().addTestDevice("14B1C04D47670D84DE173A350418C2B4").build();//build();
        //addTestDevice("14B1C04D47670D84DE173A350418C2B4").build();
        adViewBanner.setAdListener(new AdListener() {
            @Override
            public void onAdLoaded() {
                // Code to be executed when an ad finishes loading.
                Log.i("Ads", "onAdLoaded FAQs");
            }

            @Override
            public void onAdFailedToLoad(int errorCode) {
                // Code to be executed when an ad request fails.
                Log.i("Ads", "onAdFailedToLoad FAQs" + Integer.toString(errorCode));
            }


            @Override
            public void onAdLeftApplication() {
                // Code to be executed when the user has left the app.
                if(mFireBaseAnalytics != null ){
                    Bundle bundleAdBannerClicked = new Bundle();
                    bundleAdBannerClicked.putString(LPG_Utility.PARAMETER_ANALYTICS_EVENT_PARAM,LPG_Utility.CLICK_ADMOB );
                    bundleAdBannerClicked.putString(LPG_Utility.PARAMETER_ANALYTICS_ACTIVITY_PARAM,"MainActivity");
                    mFireBaseAnalytics.logEvent(LPG_Utility.CLICK_ADMOB,bundleAdBannerClicked );
                }

            }



        });
        adViewBanner.loadAd(adRequest);
    }

    public boolean showInterStitialAd() {
        boolean flagShowInterstitial = ifWeCanShowInterstitialAdNow();
        boolean showAd = mInterstitialAd.isLoaded() && flagShowInterstitial;

        if (showAd) {

            mInterstitialAd.show();

        }

        return showAd;

    }

    private void loadInterstitialAd() {
        mInterstitialAd = new InterstitialAd(getApplicationContext());
        mInterstitialAd.setAdUnitId(getResources().getString(R.string.AdMob_InterstitialAd_FAQs));
        mInterstitialAd.setAdListener(new AdListener() {
            @Override
            public void onAdFailedToLoad(int errorCode) {
                Log.v("Ads", " Failed to Load Interstitial error " + errorCode);
            }

            @Override
            public void onAdLoaded() {
                Log.v("Ads", " Interstitial Ad Loaded ");
            }

            @Override
            public void onAdClosed() {
                Intent homeActivity = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(homeActivity);
            }


        });
        mInterstitialAd.loadAd(new AdRequest.Builder().addTestDevice(getResources().getString(R.string.AdMob_TestDevice)).build());
    }

}
