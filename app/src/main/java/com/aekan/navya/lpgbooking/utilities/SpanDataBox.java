package com.aekan.navya.lpgbooking.utilities;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.text.style.URLSpan;
import android.util.Log;
import android.view.View;

import com.aekan.navya.lpgbooking.R;

/**
 * Created by arunramamurthy on 09/11/17.
 * This is a box which will hold values to create
 * clickable links for answers given in FAQs page
 * - Technically, this holds data to set URLSpan on child
 * views of SimpeExpandableListAdapter
 */

public class SpanDataBox {
    private String mURL;
    private int mStartOfSpan;
    private int mEndOfSpan;
    private URLSpan mURLSpan;
    private Context mContext;

    public SpanDataBox(Context context, String URL, int StartofSpan, int EndOfSpan) {
        this.mContext = context;
        this.mStartOfSpan = StartofSpan;
        this.mEndOfSpan = EndOfSpan;
        this.mURL = URL;

        //set URL Span
        setURLSpan(this.mURL);
    }

    public URLSpan getURLSpan() {
        return mURLSpan;
    }

    public void setURLSpan(final String URL) {
        mURLSpan = new URLSpan(URL) {
            @Override
            public void onClick(View view) {
                new LPG_AlertBoxClass().showDialogHelper(mContext.getResources().getString(R.string.FAQs_Click_Link),
                        "Ok",
                        "Cancel",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                                Intent redirectToLPGProvider = new Intent(Intent.ACTION_VIEW);
                                redirectToLPGProvider.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                redirectToLPGProvider.setData(Uri.parse(URL));
                                mContext.startActivity(redirectToLPGProvider);
                                dialog.dismiss();
                                mContext.startActivity(redirectToLPGProvider);
                            }
                        },
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        }

                ).show(((AppCompatActivity) mContext).getSupportFragmentManager(), "Alert for Redirection to LPG Providers site");
            }
        };


    }

    public String getURL() {
        return mURL;
    }

    public void setURL(String mURL) {
        this.mURL = mURL;
    }

    public int getStartOfSpan() {
        return mStartOfSpan;
    }

    public void setStartOfSpan(int mStartOfSpan) {
        this.mStartOfSpan = mStartOfSpan;
    }

    public int getEndOfSpan() {
        return mEndOfSpan;
    }

    public void setEndOfSpan(int mEndOfSpan) {
        this.mEndOfSpan = mEndOfSpan;
    }

}
