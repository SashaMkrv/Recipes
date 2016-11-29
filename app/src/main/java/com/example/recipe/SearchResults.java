package com.example.recipe;

import android.app.ListActivity;
import android.content.Intent;
import android.media.tv.TvContract;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.ContextMenu;
import android.view.MenuInflater;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ListView;

public class SearchResults extends AppCompatActivity{

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
        RecipeContainer[] resultList = new RecipeContainer[resultIDs.length];
        int idx = 0;
        for(long id: resultIDs){
            resultList[idx] = db.getRecipe(id);
            idx++;
        }

        for(RecipeContainer r:resultList){
            try{Log.i("recipe name", r.getName());}
            catch(Exception e){Log.i("problem with ", "problems");}
        }

        //String[] resultList = {"Pumpkin Pie", "Cookies", "Sandwich", "Bread", "Carrot Potato Onion"};
        RecipeArrayAdapter recipes = new RecipeArrayAdapter(this, resultList);
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
    }
    @Override
    public void onCreateContextMenu(ContextMenu menu, View v,
                                    ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.search_context_menu, menu);
    }
}
