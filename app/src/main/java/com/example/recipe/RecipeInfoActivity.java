package com.example.recipe;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

public class RecipeInfoActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_recipe_info);
        String[] egInst = {"1. mix everything", "2. into the oven", "3. consume"};
        String[] egIng = {"pumpkin", "flour", "tomato", "chicken stock"};

        LayoutInflater inflate = LayoutInflater.from(this);

        //set all those ingredients
        LinearLayout ingLayout = (LinearLayout) findViewById(R.id.ingredientList);
        ListHelper.addStringsToLinearLayout(egIng, ingLayout, inflate);

        //set all those instructions
        LinearLayout instLayout = (LinearLayout) findViewById(R.id.instructionList);
        ListHelper.addStringsToLinearLayout(egInst, instLayout, inflate);

        //set some names
        TextView name = (TextView) findViewById(R.id.recipeName);
        TextView category = (TextView) findViewById(R.id.categoryName);
        TextView type = (TextView) findViewById(R.id.typeName);
        name.setText("Dry Soup");
        type.setText("brunch");
        category.setText("underground");
    }

    public void onEditClick(View view){
        Intent intent = new Intent(this, EditRecipeActivity.class);
        startActivity(intent);
    }
}
