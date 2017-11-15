package com.aekan.navya.lpgbooking.utilities;

import android.content.Context;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.aekan.navya.lpgbooking.R;

/**
 * Created by arunramamurthy on 26/08/17.
 */

public class LPG_SpinnerAdapter extends ArrayAdapter<String> {

    private String[] mAdapterArray;
    private Context mContext;
    private LayoutInflater mInflater;

    //constructor for Array Adapter
    public LPG_SpinnerAdapter(@NonNull Context context, @LayoutRes int resource, @NonNull String[] objects) {

        super(context, resource, objects);
        mAdapterArray = objects;
        mContext = context;
        mInflater = LayoutInflater.from(mContext);
    }

    public LPG_SpinnerAdapter(@NonNull Context context, @LayoutRes int resource, int view, @NonNull String[] objects){
        super(context, resource,view,objects);
        mAdapterArray = objects;
        mContext = context;
        mInflater = LayoutInflater.from(mContext);

    }

    @Override
    public View getDropDownView(int position, @Nullable View convertView,
                                @NonNull ViewGroup parent){
        View view = new View(mContext);

        view = mInflater.inflate(R.layout.lpg_spinner_dropdown, parent, false);

        TextView text = (TextView) view.findViewById(R.id.dropdown);
        text.setText(mAdapterArray[position]);

        return  view;
    }
}
