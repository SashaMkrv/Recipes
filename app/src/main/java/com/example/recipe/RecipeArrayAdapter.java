package com.example.recipe;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;


public class RecipeArrayAdapter extends ArrayAdapter<String> {
    private final Context context;
    private final String[] values;
    public RecipeArrayAdapter(Context context, String[] values) {
        super(context, R.layout.recipe_inflate, values);
        this.context = context;
        this.values = values;
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

