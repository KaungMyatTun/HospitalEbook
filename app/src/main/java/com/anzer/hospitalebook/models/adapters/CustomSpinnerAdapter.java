package com.anzer.hospitalebook.models.adapters;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.SpinnerAdapter;
import android.widget.TextView;

import com.anzer.hospitalebook.R;

import java.util.ArrayList;


/**
 * Created by David on 3/14/2018.
 */

public class CustomSpinnerAdapter extends BaseAdapter implements SpinnerAdapter {

    private final Context activity;
    private ArrayList<String> spinlist;

    public CustomSpinnerAdapter(Context context, ArrayList<String> spinlist) {
        this.spinlist=spinlist;
        activity = context;
    }

    public int getCount()
    {
        return spinlist.size();
    }

    public Object getItem(int i)
    {
        return spinlist.get(i);
    }

    public long getItemId(int i)
    {
        return (long)i;
    }

    @Override
    public View getDropDownView(int position, View convertView, ViewGroup parent) {
        TextView txt = new TextView(convertView.getRootView().getContext());
        txt.setPadding(16, 16, 16, 16);
        txt.setTextSize(18);
        txt.setTypeface(null, Typeface.BOLD);
        txt.setGravity(Gravity.CENTER_VERTICAL);
        txt.setText(spinlist.get(position));
        txt.setTextColor(Color.parseColor("#000000"));
        return  txt;
    }

    public View getView(int i, View view, ViewGroup viewgroup) {
        TextView txt = new TextView(view.getContext());
        txt.setGravity(Gravity.CENTER);
        txt.setPadding(16, 16, 16, 16);
        txt.setTextSize(16);
        txt.setTypeface(null, Typeface.BOLD);
        txt.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_down, 0);
        txt.setText(spinlist.get(i));
        txt.setTextColor(Color.parseColor("#000000"));
        return  txt;
    }

}