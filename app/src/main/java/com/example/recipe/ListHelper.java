package com.example.recipe;

import android.view.LayoutInflater;
import android.widget.LinearLayout;
import android.widget.TextView;

/**
 * Created by sasha on 2016-11-26.
 */

public class ListHelper {
    static public void addStringsToLinearLayout(String[] list, LinearLayout layout, LayoutInflater inflate){
        for(String item : list){
            TextView view  = (TextView)inflate.inflate(android.R.layout.simple_expandable_list_item_1, layout, false);
            view.setText(item);
            layout.addView(view);
        }
    }
}
