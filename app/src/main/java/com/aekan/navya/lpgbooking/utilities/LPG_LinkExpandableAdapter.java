package com.aekan.navya.lpgbooking.utilities;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.method.LinkMovementMethod;
import android.text.style.URLSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SimpleExpandableListAdapter;
import android.widget.TextView;


import java.net.URL;
import java.util.List;
import java.util.Map;

/**
 * Created by arunramamurthy on 19/10/17.
 */

public class LPG_LinkExpandableAdapter extends SimpleExpandableListAdapter {

    private LayoutInflater mInflater;
    private int mChildLayout;
    private int[] mChildID;
    private String[] mChildKey;
    private Context mContext;
    private List<? extends List<? extends Map<String, ?>>> mChildData;
    private List<? extends List<? extends Map<String, ?>>> mChildUrlSpan;

    public LPG_LinkExpandableAdapter(Context context,
                                     List<? extends Map<String, ?>> groupData,
                                     int groupLayout,
                                     String[] groupFrom,
                                     int[] groupTo,
                                     List<? extends List<? extends Map<String, ?>>> childData,
                                     int childLayout,
                                     String[] childFrom,
                                     int[] childTo,
                                     List<? extends List<? extends Map<String, ? extends List<SpanDataBox>>>> childURLSpan
    ) {
        super(context, groupData, groupLayout, groupFrom, groupTo, childData, childLayout, childFrom, childTo);

        mChildData = childData;
        mChildID = childTo;
        mChildLayout = childLayout;
        mChildUrlSpan = childURLSpan;
        mChildKey = childFrom;
        mContext = context;
        mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        View view;

            //inflate view from child layout
            view = mInflater.inflate(mChildLayout, parent, false);
            //bind values to view, along with data span
            bindChildView(view, groupPosition, childPosition);
            return view;


    }

    private void bindChildView(View view, int groupPosition, int childPosition) {
        //Get strings associated with the child position
        Map<String, ?> childElements = mChildData.get(groupPosition).get(childPosition);
        //get the URL associated with the child position
        Map<String, ?> childSpan = mChildUrlSpan.get(groupPosition).get(childPosition);

        int countOfChildElements = mChildID.length;

        //bind the strings as well as URLs
        for (int i = 0; i < countOfChildElements; ++i) {

            TextView textView = (TextView) view.findViewById(mChildID[i]);
            if (textView != null) {

              List<SpanDataBox> listAttachSpan = (List<SpanDataBox>) childSpan.get(mChildKey[i]);
                if (listAttachSpan != null) {


                    SpannableStringBuilder answerSpanBuilder = new SpannableStringBuilder((CharSequence) childElements.get(mChildKey[i]));
                    for (int j = 0; j < listAttachSpan.size(); ++j) {
                        SpanDataBox answerSpanData = listAttachSpan.get(j);

                        answerSpanBuilder.setSpan(answerSpanData.getURLSpan(), answerSpanData.getStartOfSpan(), answerSpanData.getEndOfSpan(), SpannableStringBuilder.SPAN_EXCLUSIVE_EXCLUSIVE);
                    }


                    textView.setText(answerSpanBuilder, TextView.BufferType.SPANNABLE);

                    textView.setMovementMethod(LinkMovementMethod.getInstance());
                } else {
                    textView.setText((CharSequence) childElements.get(mChildKey[i]));

                    //
                }

            }


        }

    }
}
