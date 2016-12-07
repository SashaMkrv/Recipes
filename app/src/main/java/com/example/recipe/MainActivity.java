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
import java.util.HashMap;
import java.util.List;
import java.util.Scanner;

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
    private int ADD_RECIPE = 0;

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
                EditText rName = (EditText)findViewById(R.id.recipeName);
                String name = (rName).getText().toString();
                long id = db.newRecipe(name);
                intent.putExtra("id", id);
                startActivityForResult(intent, ADD_RECIPE);

                rName.setText("");
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent){
        if(requestCode == ADD_RECIPE){
            //i.e. in the event that the recipe goes unsaved
            if(resultCode == Activity.RESULT_CANCELED){
                long id = intent.getLongExtra("id", -1);
                if (id > -1){
                    RecipeDBHelper db = new RecipeDBHelper(this);
                    db.deleteRecipe(id);
                    Log.i("id", ""+id);
                }
            }
            Log.i("id", "made it somewhere");
        }
    }

    public void onImageClick(View view){
        Intent intent = new Intent(this, ChangeImageActivity.class);
        startActivity(intent);
    }

    public long[] calculateRelevance(){
        RecipeDBHelper db = new RecipeDBHelper(this);
        TextView ingredients = (TextView) findViewById(R.id.ingredientSearch);
        if(ingredients.getText().toString().equals("")) {
            return db.getAll();
        }
        return db.searchIngredient(ingredients.getText().toString());
        /**Scanner scan = new Scanner(ingredients.getText().toString());
        ArrayList<long[]> ids = new ArrayList<long[]>();
        ArrayList<String> terms = new ArrayList<String>();
        String term;
        HashMap<Long, Integer> vals = new HashMap<Long, Integer>();
        ArrayList<String> dontTerm = new ArrayList<String>();
        while(scan.hasNext()) {
                switch(term = scan.next()) {
                    case "AND":
                    //case "OR":
                    case "NOT": dontTerm.add(scan.next());

                    default: terms.add(term);
                        ids.add(db.searchIngredient(term));
                }
            }
        }*/
    }
}