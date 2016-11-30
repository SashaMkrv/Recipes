package com.example.recipe;

import android.app.ListActivity;
import android.content.Intent;
import android.media.tv.TvContract;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.ContextMenu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.Arrays;

public class SearchResults extends AppCompatActivity{
    private ListView list;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setTitle(R.string.searchPageName);
        setContentView(R.layout.activity_search_results);

        RecipeDBHelper db = new RecipeDBHelper(this);

        long[] resultIDs = getIntent().getLongArrayExtra("ids");
        for(long r:resultIDs){
            Log.i("id", r+"");
        }

        //long[] resultIDs = db.searchIngredient("Apples"); //an example list of items
        RecipeContainer[] resultList;
        ArrayList<RecipeContainer> rList = new ArrayList<RecipeContainer>();
        int idx = 0;
        for(long id: resultIDs){
            RecipeContainer tmp = db.getRecipe(id);
            if (!(tmp == null)){
                rList.add(tmp);
            }
        }

        resultList = rList.toArray(new RecipeContainer[rList.size()]);

        for(RecipeContainer r:resultList){
            try{Log.i("recipe name", r.getName());}
            catch(Exception e){Log.i("problem with ", "problems");}
        }

        //String[] resultList = {"Pumpkin Pie", "Cookies", "Sandwich", "Bread", "Carrot Potato Onion"};
        RecipeArrayAdapter recipes = new RecipeArrayAdapter(this, Arrays.asList(resultList));
        ListView results = (ListView)findViewById(R.id.resultList);
        results.setAdapter(recipes);
        results.setOnItemClickListener(new AdapterView.OnItemClickListener(){
            @Override
            public void onItemClick(AdapterView<?> parent, final View view, int position, long l){
                Intent intent = new Intent(getApplicationContext(), RecipeInfoActivity.class);
                RecipeArrayAdapter adapter = (RecipeArrayAdapter)parent.getAdapter();
                intent.putExtra("id", adapter.ids[position]);
                startActivity(intent);
            }
        });
        registerForContextMenu(results);
        list = results;
    }
    @Override
    public void onCreateContextMenu(ContextMenu menu, View v,
                                    ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.search_context_menu, menu);
    }

    public boolean onContextItemSelected(MenuItem menu){
        int idx = ((AdapterView.AdapterContextMenuInfo)menu.getMenuInfo()).position;
        RecipeArrayAdapter adapter = (RecipeArrayAdapter)list.getAdapter();
        RecipeContainer recipe = adapter.getItem(idx);
        long id = recipe.getRecipeid();
        RecipeDBHelper db = new RecipeDBHelper(this);
        Log.i("name", recipe.getName());
        if(menu.getTitle().toString().equals("Delete")){
            Log.i("delete", "item "+id);
            db.deleteRecipe(id);
            //adapter.remove(adapter.getItem(idx));
            // ^ will not work with arrays in adapters
            //unsure if converting all stored values to arraylists would work though
        }
        else{
            Intent intent = new Intent(this,EditRecipeActivity.class);
            intent.putExtra("id", id);
            startActivity(intent);
        }
        return true;
    }

}
