package com.example.recipe;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.Arrays;
import java.util.List;


public class RecipeArrayAdapter extends ArrayAdapter<RecipeContainer> {
    private final Context context;
    private final String[] values;
    public final long[] ids;
    public RecipeArrayAdapter(Context context, List<RecipeContainer> values) {
        super(context, R.layout.recipe_inflate, values);
        String[] names = new String[values.size()];
        int idx = 0;
        for (RecipeContainer r: values){
            names[idx] = r.getName();
            idx++;
        }
        long[] ids = new long[values.size()];
        idx = 0;
        for (RecipeContainer r: values){
            ids[idx] = r.getRecipeid();
            idx++;
        }
        this.ids = ids;
        this.context = context;
        this.values = names;
    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rowView = inflater.inflate(R.layout.recipe_inflate, parent, false);
        TextView textView = (TextView) rowView.findViewById(R.id.recipeName);
        ImageView imageView = (ImageView) rowView.findViewById(R.id.recipeImage);
        textView.setText(values[position]);
// Change the icon for Windows and iPhone
        String s = values[position];
        if (s == null || s.isEmpty() || s.equals("empty")) {
            imageView.setImageResource(R.drawable.avatar_orange);
        } else {
            imageView.setImageResource(R.drawable.avatar_orange);
        }
        return rowView;
    }
}

