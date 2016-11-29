package com.example.recipe;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.support.annotation.IntegerRes;
import android.util.Log;

import org.json.JSONArray;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class RecipeDBHelper extends SQLiteOpenHelper {
    static public long egRecipeid;
    static private boolean filled = false;

    static private String RECIPE_TABLE_NAME = "recipes";
    static private String INGREDIENTS_TABLE_NAME = "ingredients";
    static private String INGREDIENT_COLUMN_NAME = "ingredientName";

    public RecipeDBHelper(Context context){
        super(context, "recipesDB", null, 1);
    }
    /**public RecipeDBHelper(Context context, String string, SQLiteDatabase.CursorFactory cf, int i){
        super(context, "recipesDB", null, 1);
    }*/

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(
                "create table " + RECIPE_TABLE_NAME +
                " (recipeid integer primary key,"+
                "recipeName text," +
                        "recipeType varchar(20)," +
                        "recipeCategory varchar(20)," +
                        "recipeInstructions text);");
        db.execSQL("create table " + INGREDIENTS_TABLE_NAME +
                " (" + INGREDIENT_COLUMN_NAME + " varchar(20)," +
                "recipeid integer," +
                "PRIMARY KEY("+INGREDIENT_COLUMN_NAME+", recipeid),"+
                "FOREIGN KEY(recipeid) REFERENCES recipes(recipeid) ON DELETE CASCADE);");

        }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // TODO Auto-generated method stub
        db.execSQL("DROP TABLE IF EXISTS "+ RECIPE_TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS "+ INGREDIENTS_TABLE_NAME);
        onCreate(db);
    }

    public void fillExamples(){
        if (filled){return;}

        long id = this.newRecipe("Apple Pie");
        this.updateRecipe(id, "Apple Pie", "Desert", "North American", new String[] {"flour", "butter", "eggs", "apples", "sugar"},
                new String[] {"Make pie dough.", "Refrigerate dough.", "Roll dough into mold.", "Put in apples.", "Bake at 450 f for 20 minutes.", "Let cool"});

        id = this.newRecipe("Apple salad");
        this.updateRecipe(id, "Apple Salad", "Side", "Who Knows", new String[] {"apples", "mayonnaise", "carrots", "cabbage", "raisins"},
                new String[] {"Mix in all 'dry' ingredients.", "Mix in mayonnaise.", "Serve."});

        id = this.newRecipe("Apple Oatmeal");
        this.updateRecipe(id, "Apple Oatmeal", "Breakfast", "Who Knows", new String[] {"oatmeal", "sugar", "milk", "apples", "cinnamon"},
                new String[] {"Bring milk to a boil", "Put oatmeal in the milk", "Stir in sugar, apples, and cinnammon", "Simmer for 5 minutes"});

        egRecipeid = id;
        filled = true;
    }

    /**public void createRecipe(String name, String type, String category,
                           String[] instructions, String[] ingredients){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put("recipes.recipeName", name);
        cv.put("recipes.recipeType", type);
        cv.put("recipes.recipeCategory", category);
        cv.put("recipes.recipeInstructions",
                (new JSONArray(Arrays.asList(instructions)).toString()));
        long id = db.insert("recipesDB", null, cv);
        for(String ing: ingredients){
            cv = new ContentValues();
            cv.put("ingredients.recipeid", id);
            cv.put("ingredients.ingredientName", ing);
            db.insert("recipesDB", null, cv);
        }
    }*/

    public long newRecipe(String name){
        ContentValues cv = new ContentValues();
        cv.put("recipeName", name);
        return this.getWritableDatabase().insert("recipes", null, cv);
    }

    public void updateRecipe(RecipeContainer rc){
        this.updateRecipe(rc.getRecipeid(), rc.getName(), rc.getType(), rc.getCategory(), rc.getIngredients(), rc.getInstructions());
    }

    public void updateRecipe(long id, String name, String type, String category, String[] ingredients, String[] instructions){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put("recipeid", id);
        cv.put("recipeName", name);
        cv.put("recipeType", type);
        cv.put("recipeCategory", category);
        cv.put("recipeInstructions",
                (new JSONArray(Arrays.asList(instructions)).toString()));
        db.replace(RECIPE_TABLE_NAME, null, cv);
        //delete ingredients before writing current list
        db.delete(INGREDIENTS_TABLE_NAME, "recipeid = " + id, null);
        for(String ing: ingredients){
            cv = new ContentValues();
            cv.put("recipeid", id);
            cv.put("ingredientName", ing);
            db.insert(INGREDIENTS_TABLE_NAME, null, cv);
        }
    }

    static private long[] getIds(Cursor results, String column){
        long[] ids = new long[results.getCount()];
        int idx = 0;
        if (results == null | results.getCount() == 0){return ids;}
        while(!results.isAfterLast()){
            ids[idx] = results.getLong(results.getColumnIndex(column));
            idx++;
            results.moveToNext();
        }
        results.close();
        return ids;
    }

    static private String[] getStrings(Cursor results, String column){
        String[] strings = new String[results.getCount()];
        int idx = 0;
        int index = results.getColumnIndex(column);
        if (results == null | results.getCount() == 0){return strings;}
        while(!results.isAfterLast()){
            strings[idx] = results.getString(index);
            idx++;
            results.moveToNext();
        }
        results.close();
        return strings;
    }

    public long[] searchIngredient(String ingredient){
        SQLiteDatabase db = this.getReadableDatabase();
        long[] ids;
        String[] columns = {"recipeid"};

        Cursor results = db.query(true, INGREDIENTS_TABLE_NAME, columns, "ingredientName = " + ingredient, null, null, null, null, null);
        results.moveToFirst();
        return getIds(results, columns[0]);
    }

    public long[] searchCategory(String category){
        SQLiteDatabase db = this.getReadableDatabase();
        long[] ids;
        String[] columns = {"recipeid"};

        Cursor results = db.query(true, RECIPE_TABLE_NAME, columns, "categoryName = " + category, null, null, null, null, null);
        results.moveToFirst();

        return getIds(results, columns[0]);
    }

    public long[] searchType(String type){
        SQLiteDatabase db = this.getReadableDatabase();
        long[] ids;
        String[] columns = {"recipeid"};

        Cursor results = db.query(true, RECIPE_TABLE_NAME, columns, "typeName = " + type, null, null, null, null, null);
        results.moveToFirst();

        return getIds(results, columns[0]);
    }

    public RecipeContainer getRecipe(long id){
        SQLiteDatabase db = this.getReadableDatabase();
        RecipeContainer recipe;
        JSONArray jsoninstructions;

        Cursor results = db.query(true, RECIPE_TABLE_NAME, new String[] {"recipeName",
        "recipeType", "recipeCategory", "recipeInstructions"}, "recipeid = "+id, null, null, null, null, null);

        results.moveToFirst();
        recipe = new RecipeContainer(id, results.getString(0), results.getString(1), results.getString(2));
        String inst = results.getString(3);
        Log.i("instruction", inst);
        try{jsoninstructions = new JSONArray(results.getString(3));}
        catch (Exception e) {jsoninstructions = new JSONArray();};


        for(int i = 0; i < jsoninstructions.length(); i++){
            try{ recipe.addInstruction(jsoninstructions.getString(i));}
            catch (Exception e){recipe.addInstruction("");}
        }


        results = db.query(true, INGREDIENTS_TABLE_NAME, new String[] {INGREDIENT_COLUMN_NAME}, "recipeid = "+id, null, null, null, null, null);

        results.moveToFirst();
        String[] ingredients = getStrings(results, INGREDIENT_COLUMN_NAME);

        for(String ing:ingredients){
            recipe.addInstruction(ing);
        }

        return recipe;

    }

    public void deleteRecipe(long id){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        db.delete("recipes", "recipeid = " + id, null);
    }
}
