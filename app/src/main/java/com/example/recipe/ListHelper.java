package com.example.recipe;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewParent;
import android.widget.LinearLayout;
import android.widget.TextView;

/**
 * Created by sasha on 2016-11-26.
 */

public class ListHelper {
    static public void addStringsToLinearLayout(String[] list, LinearLayout layout, LayoutInflater inflate, boolean clickable){
        for(String item : list){
            TextView view  = (TextView)inflate.inflate(android.R.layout.simple_expandable_list_item_1, layout, false);
            view.setText(item);
            if (clickable) {
                view.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        LinearLayout parent = (LinearLayout) v.getParent();
                        parent.removeView(v);
                    }
                });
            }
            layout.addView(view);
        }
    }
}
