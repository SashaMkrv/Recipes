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
    private long search_id = RecipeDBHelper.egRecipeid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_recipe_info);
        RecipeDBHelper db = new RecipeDBHelper(this);
        db.fillExamples();

        long id = getIntent().getLongExtra("id", RecipeDBHelper.egRecipeid);

        RecipeContainer recipe = db.getRecipe(id);

        search_id = recipe.getRecipeid();

        String[] egInst = recipe.getInstructions();
        String[] egIng = recipe.getIngredients();

        String nameStr = recipe.getName();
        String typeStr = recipe.getType();
        String categoryStr = recipe.getCategory();

        LayoutInflater inflate = LayoutInflater.from(this);

        //set all those ingredients
        LinearLayout ingLayout = (LinearLayout) findViewById(R.id.ingredientList);
        ListHelper.addStringsToLinearLayout(egIng, ingLayout, inflate, false);

        //set all those instructions
        LinearLayout instLayout = (LinearLayout) findViewById(R.id.instructionList);
        ListHelper.addStringsToLinearLayout(egInst, instLayout, inflate, false);

        //set some names
        TextView name = (TextView) findViewById(R.id.recipeName);
        TextView category = (TextView) findViewById(R.id.categoryName);
        TextView type = (TextView) findViewById(R.id.typeName);
        name.setText(nameStr);
        type.setText(typeStr);
        category.setText(categoryStr);
    }

    public void onEditClick(View view){
        Intent intent = new Intent(this, EditRecipeActivity.class);
        Bundle dataBundle = new Bundle();
        //dataBundle.putInt("id", (int)search_id); //this might be dangerous
        intent.putExtra("id", search_id);
        startActivity(intent);
    }
}
