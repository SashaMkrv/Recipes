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

import org.w3c.dom.Text;

import java.util.zip.Inflater;

import static android.R.attr.id;
import static java.util.Objects.isNull;

public class EditRecipeActivity extends AppCompatActivity {
    private RecipeContainer recipe;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setTitle(R.string.editPageName);
        setContentView(R.layout.activity_edit_recipe);

        long id = getIntent().getLongExtra("id", RecipeDBHelper.egRecipeid);

        RecipeDBHelper db = new RecipeDBHelper(this);
        try{recipe = db.getRecipe(id);}
        catch (Exception e){
            recipe = new RecipeContainer(id, "Error", "No Category", "No Type");
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
        ListHelper.addStringsToLinearLayout(egIng, ingLayout, inflate, true);

        //set all those instructions
        LinearLayout instLayout = (LinearLayout) findViewById(R.id.instructionList);
        ListHelper.addStringsToLinearLayout(egInst, instLayout, inflate, true);

        ((EditText)findViewById(R.id.recipeName)).setText(nameStr);
        ((EditText)findViewById(R.id.typeName)).setText(typeStr);
        ((EditText)findViewById(R.id.categoryName)).setText(categoryStr);

    }
    public void onImageClick(View view){
        Intent intent = new Intent(this, ChangeImageActivity.class);
        startActivity(intent);
    }

    public void onSaveButtonClick(View view){
        recipe.setName((((EditText)findViewById(R.id.recipeName)).getText()).toString());
        recipe.setType(((EditText)findViewById(R.id.typeName)).getText().toString());
        recipe.setCategory(((EditText)findViewById(R.id.categoryName)).getText().toString());
        recipe.clearIngredients();
        recipe.clearInstructions();
        LinearLayout ingredients = (LinearLayout)findViewById(R.id.ingredientList);

        int count = ingredients.getChildCount();
        for (int i = 0; i < count; i++) {
            TextView row = (TextView)ingredients.getChildAt(i);
            if (!(row==null)) {
                recipe.addIngredient(row.getText().toString());
            }
        }

        LinearLayout instructions = (LinearLayout) findViewById(R.id.instructionList);

        count = instructions.getChildCount();
        for (int i = 0; i < count; i++){
            TextView row = (TextView) instructions.getChildAt(i);
            if (!(row == null)) {
                recipe.addInstruction(row.getText().toString());
            }
        }

        RecipeDBHelper db = new RecipeDBHelper(this);
        db.updateRecipe(recipe);
        finish();
    }

    public void onAddIngredientButtonClick(View view){
        LinearLayout ingLayout = (LinearLayout) findViewById(R.id.ingredientList);
        LayoutInflater inflate = LayoutInflater.from(view.getContext());
        EditText ing = (EditText)findViewById(R.id.addIngredientText);
        String[] addIng = {ing.getText().toString()};
        ListHelper.addStringsToLinearLayout(addIng, ingLayout, inflate, true);
        ing.setText("");
    }

    public void onAddInstructionButtonClick(View view){
        LinearLayout instLayout = (LinearLayout) findViewById(R.id.instructionList);
        LayoutInflater inflate = LayoutInflater.from(view.getContext());
        EditText inst = (EditText)findViewById(R.id.addInstructionText);
        String[] addInst = {inst.getText().toString()};
        ListHelper.addStringsToLinearLayout(addInst, instLayout, inflate, true);
        inst.setText("");
    }
}
