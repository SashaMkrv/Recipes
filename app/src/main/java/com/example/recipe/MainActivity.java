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
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Scanner;
import java.util.Set;

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
import android.widget.RadioButton;
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
                }
            }
        }
    }

    public void onImageClick(View view){
        Intent intent = new Intent(this, ChangeImageActivity.class);
        startActivity(intent);
    }

    public long[] calculateRelevance(){
        long[] ids;
        RecipeDBHelper db = new RecipeDBHelper(this);
        TextView ingredients = (TextView) findViewById(R.id.ingredientSearch);
        if(ingredients.getText().toString().equals("")) {
            ids = db.getAll();
        }
        else{
            ArrayList<Long> idGroups = new ArrayList<Long>();
            Set<Long> currentGroup = new HashSet<Long>();
            Scanner scan = new Scanner(ingredients.getText().toString());
            /**ArrayList<long[]> ids = new ArrayList<long[]>();
             ArrayList<String> terms = new ArrayList<String>();
             String term;
             HashMap<Long, Integer> vals = new HashMap<Long, Integer>();
             ArrayList<String> dontTerm = new ArrayList<String>();
             */
            String term;
            while(scan.hasNext()) {
                switch(term = scan.next()) {
                    case "AND":
                        idGroups.addAll(currentGroup);
                        currentGroup.clear();
                        break;
                    //case "OR":
                    case "NOT":
                        if (scan.hasNext()) currentGroup.addAll(getSearchIDs(true, scan.next(), db));
                        break;
                    default:
                        currentGroup.addAll(getSearchIDs(false, term, db));
                }
            }
            idGroups.addAll(currentGroup);


            //EditText catOrType = (EditText)findViewById(R.id.typeSearch);


            //uses a hash map to calculate the number of times each recipe appears
            HashMap<Long, Integer> rank = new HashMap<Long, Integer>();
            Long id;
            Iterator<Long> iter = idGroups.iterator();
            while(iter.hasNext()) {
                id = iter.next().longValue();
                if (rank.containsKey(id)) rank.put(id, rank.get(id) + 1);
                else rank.put(id, 1);
            }
            Set<Long> allIds = new HashSet<>();
            allIds.addAll(idGroups);
            List<Long> allIdsL = new ArrayList<Long>(allIds);
            //to avoid destroying the iterator
            iter = allIds.iterator();
            Long check;
            while (iter.hasNext()){
                check = iter.next();
                if (rank.get(check) < 1){
                    allIdsL.remove(check);
                }
                //remove all results with less than 1 match
                //this actually probably not needed, nothing should be appearing without any matches
            }
            Collections.sort(allIdsL, Collections.reverseOrder(new IdCompare(rank)));
            long[] finalIds = new long[allIdsL.size()];
            for (int i = 0; i < finalIds.length; i++){
                finalIds[i] = allIdsL.get(i);
            }
            ids = finalIds;
        }
        //if there is a category or type, get rid of irrelevant recipes
        EditText cat = (EditText)findViewById(R.id.typeSearch);
        if(!cat.getText().toString().equals("")){
            RadioButton catCheck = (RadioButton)findViewById(R.id.categoryCheck);
            if (catCheck.isSelected()) ids =  db.shrinkByCategory(ids, cat.getText().toString());
            else ids  =  db.shrinkByType(ids, cat.getText().toString());
        }
        return ids;
    }
    //managing the array to ArrayList conversion and search query.
    private ArrayList<Long> getSearchIDs(boolean not, String term, RecipeDBHelper db){
        long[] tmp = db.searchIngredient(term, not);
        ArrayList<Long> returnValues = new ArrayList<Long>(tmp.length);
        for(long l: tmp){
            returnValues.add(l);
        }
        return returnValues;
    }
    //custome comparator for ordering recipes
    private class IdCompare implements Comparator<Long> {
        HashMap<Long, Integer> hm;
        IdCompare(HashMap h){
            hm = h;
        }
        public int compare(Long l1, Long l2){
            int tmp =  hm.get(l1) - hm.get(l2);
            if (tmp == 0){
                if (l1 < l2) return 1;
                else return -1;
            }
            else return tmp;
        }
    }
}