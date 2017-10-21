package com.aekan.navya.lpgbooking.utilities;

import android.content.Context;
import android.view.LayoutInflater;
import android.widget.SimpleExpandableListAdapter;

import java.util.List;
import java.util.Map;

/**
 * Created by arunramamurthy on 19/10/17.
 */

class LPG_LinkExpandableAdapter extends SimpleExpandableListAdapter {

    private LayoutInflater mInflater;

    public LPG_LinkExpandableAdapter(Context context, List<? extends Map<String, ?>> groupData, int groupLayout, String[] groupFrom, int[] groupTo, List<? extends List<? extends Map<String, ?>>> childData, int childLayout, String[] childFrom, int[] childTo) {
        super(context, groupData, groupLayout, groupFrom, groupTo, childData, childLayout, childFrom, childTo);

        mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }
}
