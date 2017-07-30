package com.aekan.navya.lpgbooking.utilities;

import android.app.ExpandableListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ExpandableListAdapter;
import android.widget.SimpleExpandableListAdapter;

import com.aekan.navya.lpgbooking.MainActivity;
import com.aekan.navya.lpgbooking.R;

/**
 * Created by arunramamurthy on 26/07/17.
 */

public class FAQs extends ExpandableListActivity {
    protected void onCreate(Bundle savedInstance) {
        super.onCreate(savedInstance);

        //inflate view
        setContentView(R.layout.faqs);

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

        //create expandable list adapter
        ExpandableListAdapter adapterFAQs = new SimpleExpandableListAdapter(getApplicationContext(),
                LPG_Utility.getExpandableGroupData(getApplicationContext()),
                R.id.faq_question,
                new String[]{LPG_Utility.EXPN_LIST_QUESTION},
                new int[]{R.id.faq_question},
                LPG_Utility.getExpandableChildData(getApplicationContext()),
                R.id.faq_question,
                new String[]{LPG_Utility.EXPN_LIST_ANSWER},
                new int[]{R.id.faq_answer}
        );
        //set Adpater
        setListAdapter(adapterFAQs);
    }
}
