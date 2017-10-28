package com.aekan.navya.lpgbooking;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.SimpleExpandableListAdapter;

import com.aekan.navya.lpgbooking.utilities.LPG_Utility;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

/**
 * Created by arunramamurthy on 26/07/17.
 */

public class FAQs extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstance) {
        super.onCreate(savedInstance);

        // inflate view
        setContentView(R.layout.faqs);
        Log.v("FAQs ", " I got inside");
        //Set tool bar for the activity
        Toolbar toolbar = (Toolbar) findViewById(R.id.faqs_toolbar);
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back_black_24dp);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent mainActivity = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(mainActivity);
            }
        });
        toolbar.setTitle("FAQs");
        setSupportActionBar(toolbar);
        Log.v("FAQs", " Calling adapterFAQs");
        //create expandable list adapter
        ExpandableListAdapter adapterFAQs = new SimpleExpandableListAdapter(getApplicationContext(),
                LPG_Utility.getExpandableGroupData(getApplicationContext()),
                R.layout.faq_question,
                new String[]{LPG_Utility.EXPN_LIST_QUESTION},
                new int[]{R.id.faq_question},
                LPG_Utility.getExpandableChildData(getApplicationContext()),
                R.layout.faq_answer,
                new String[]{LPG_Utility.EXPN_LIST_ANSWER},
                new int[]{R.id.faq_answer}
        );
        //set Adpater
        // setListAdapter(adapterFAQs);
        ExpandableListView faqSection = (ExpandableListView) findViewById(R.id.list);
        faqSection.setAdapter(adapterFAQs);
        //faqSection.setIndicatorBounds(30,36);

        //Banner Ad
        showBannerAd();





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
        });
        adViewBanner.loadAd(adRequest);
    }
}
