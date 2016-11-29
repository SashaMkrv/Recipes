package com.example.recipe;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.zip.Inflater;

import static android.R.attr.id;

public class EditRecipeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setTitle(R.string.editPageName);
        setContentView(R.layout.activity_edit_recipe);

        long id = getIntent().getLongExtra("id", RecipeDBHelper.egRecipeid);

        RecipeDBHelper db = new RecipeDBHelper(this);
        RecipeContainer recipe;
        try{recipe = db.getRecipe(id);}
        catch (Exception e){
            recipe = new RecipeContainer(id, "No Name", "No Category", "No Type");
        }
        Log.i("id got from intent: ", ""+id);

        String[] egInst = recipe.getInstructions();
        String[] egIng = recipe.getIngredients();
        String nameStr = recipe.getName();
        String typeStr = recipe.getType();
        String categoryStr = recipe.getCategory();

        LayoutInflater inflate = LayoutInflater.from(this);

        //set all those ingredients
        LinearLayout ingLayout = (LinearLayout) findViewById(R.id.ingredientList);
        ListHelper.addStringsToLinearLayout(egIng, ingLayout, inflate);

        //set all those instructions
        LinearLayout instLayout = (LinearLayout) findViewById(R.id.instructionList);
        ListHelper.addStringsToLinearLayout(egInst, instLayout, inflate);

        ((EditText)findViewById(R.id.recipeName)).setText(nameStr);
        ((EditText)findViewById(R.id.typeName)).setText(typeStr);
        ((EditText)findViewById(R.id.categoryName)).setText(categoryStr);

    }
    public void onImageClick(View view){
        Intent intent = new Intent(this, ChangeImageActivity.class);
        startActivity(intent);
    }
}
