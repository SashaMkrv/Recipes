package com.example.recipe;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;

import java.util.ArrayList;

import java.util.ArrayList;
import java.util.List;

import android.os.Bundle;
import android.app.Activity;
import android.app.ListActivity;
import android.view.Menu;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TabHost;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_main);

        TabHost mTabHost = (TabHost) findViewById(R.id.tabHost);
        mTabHost.setup();
        //Lets add the first Tab
        TabHost.TabSpec mSpec = mTabHost.newTabSpec("Search");
        mSpec.setContent(R.id.search_Tab);
        mSpec.setIndicator("Search");
        mTabHost.addTab(mSpec);
        //Lets add the second Tab
        mSpec = mTabHost.newTabSpec("Add Recipe");
        mSpec.setContent(R.id.add_Tab);
        mSpec.setIndicator("Add Recipe");
        mTabHost.addTab(mSpec);

        mSpec = mTabHost.newTabSpec("Info");
        mSpec.setContent(R.id.info);
        mSpec.setIndicator("Info");
        mTabHost.addTab(mSpec);

        Button searchButton = (Button) findViewById(R.id.searchButton);
        searchButton.setOnClickListener(new Button.OnClickListener() {
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), SearchResults.class);
                RecipeDBHelper db = new RecipeDBHelper(view.getContext());
                db.fillExamples();
                //intent.putExtra("ids", db.searchType("Breakfast"));
                intent.putExtra("ids", calculateRelevance());
                startActivity(intent);
            }
        });
        Button addButton = (Button) findViewById(R.id.addButton);
        addButton.setOnClickListener(new Button.OnClickListener() {
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), EditRecipeActivity.class);
                RecipeDBHelper db = new RecipeDBHelper(view.getContext());
                String name = ((EditText)findViewById(R.id.recipeName)).getText().toString();
                long id = db.newRecipe(name);
                intent.putExtra("id", id);
                startActivity(intent);
            }
        });
    }
    public void onImageClick(View view){
        Intent intent = new Intent(this, ChangeImageActivity.class);
        startActivity(intent);
    }

    public long[] calculateRelevance(){
        RecipeDBHelper db = new RecipeDBHelper(this);
        TextView ingredients = (TextView) findViewById(R.id.ingredientSearch);
        return db.searchIngredient(ingredients.getText().toString());
    }
}