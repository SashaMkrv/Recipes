package com.example.recipe;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import org.json.JSONArray;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class RecipeDBHelper extends SQLiteOpenHelper {
    public RecipeDBHelper(Context context, String string, SQLiteDatabase.CursorFactory cf, int i){
        super(context, "recipesDB", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(
                "create table recipes" +
                "(recipeid integer primary key,"+
                "recipeName text," +
                        "recipeType varchar(20)," +
                        "recipeCategory varchar(20)," +
                        "recipeInstructions text);" +
                "create table ingredients" +
                "(ingredientname varchar(20)," +
                "recipeid integer," +
                "PRIMARY KEY(ingredientName, recipeid),"+
                "FOREIGN KEY(recipeid) REFERENCES recipes(recipeid) ON DELETE CASCADE;");

        long id = this.newRecipe("Apple Pie");
        this.updateRecipe(id, "Apple Pie", "Desert", "North American", new String[] {"flour", "butter", "eggs", "apples", "sugar"},
                new String[] {"Make pie dough.", "Refrigerate dough.", "Roll dough into mold.", "Put in apples.", "Bake at 450 f for 20 minutes.", "Let cool"});

        id = this.newRecipe("Apple salad");
        this.updateRecipe(id, "Apple Salad", "Side", "Who Knows", new String[] {"apples", "mayonnaise", "carrots", "cabbage", "raisins"},
                new String[] {"Mix in all 'dry' ingredients.", "Mix in mayonnaise.", "Serve."});

        id = this.newRecipe("Apple Oatmeal");
        this.updateRecipe(id, "Apple Oatmeal", "Breakfast", "Who Knows", new String[] {"oatmeal", "sugar", "milk", "apples", "cinnamon"},
                new String[] {"Bring milk to a boil", "Put oatmeal in the milk", "Stir in sugar, apples, and cinnammon", "Simmer for 5 minutes"});
        }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

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
        return this.getWritableDatabase().insert("recipesDB", null, cv);
    }

    public void updateRecipe(RecipeContainer rc){
        this.updateRecipe(rc.getRecipeid(), rc.getName(), rc.getType(), rc.getCategory(), rc.getInstructions(), rc.getInstructions());
    }

    public void updateRecipe(long id, String name, String type, String category, String[] instructions, String[] ingredients){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put("recipes.recipeid", id);
        cv.put("recipes.recipeName", name);
        cv.put("recipes.recipeType", type);
        cv.put("recipes.recipeCategory", category);
        cv.put("recipes.recipeInstructions",
                (new JSONArray(Arrays.asList(instructions)).toString()));
        db.replace("recipesDB", null, cv);
        //delete ingredients before writing current list
        db.delete("recipesDB", "ingredients.recipeid = " + id, null);
        for(String ing: ingredients){
            cv = new ContentValues();
            cv.put("ingredients.recipeid", id);
            cv.put("ingredients.ingredientName", ing);
            db.insert("recipesDB", null, cv);
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

    public long[] searchIngredient(String ingredient){
        SQLiteDatabase db = this.getReadableDatabase();
        long[] ids;
        String[] columns = {"ingredients.recipeid"};

        Cursor results = db.query(true, "recipeDB", columns, "ingredients.ingredientName = " + ingredient, null, null, null, null, null);
        results.moveToFirst();
        return getIds(results, columns[0]);
    }

    public long[] searchCategory(String category){
        SQLiteDatabase db = this.getReadableDatabase();
        long[] ids;
        String[] columns = {"recipes.recipeid"};

        Cursor results = db.query(true, "recipeDB", columns, "recipes.categoryName = " + category, null, null, null, null, null);
        results.moveToFirst();

        return getIds(results, columns[0]);
    }

    public long[] searchType(String type){
        SQLiteDatabase db = this.getReadableDatabase();
        long[] ids;
        String[] columns = {"recipes.recipeid"};

        Cursor results = db.query(true, "recipeDB", columns, "recipes.typeName = " + type, null, null, null, null, null);
        results.moveToFirst();

        return getIds(results, columns[0]);
    }

    public RecipeContainer getRecipe(long id){
        SQLiteDatabase db = this.getReadableDatabase();
        RecipeContainer recipe;
        JSONArray jsoninstructions;

        Cursor results = db.query(true, "recipeDB", new String[] {"recipes.recipeName",
        "recipes.recipeType", "recipes.recipeCategory", "recipes.instructions"}, "recipes.recipeid = "+id, null, null, null, null, null);

        results.moveToFirst();
        recipe = new RecipeContainer(id, results.getString(0), results.getString(1), results.getString(2));
        try{jsoninstructions = new JSONArray(results.getString(3));}
        catch (Exception e) {jsoninstructions = new JSONArray();};


        for(int i = 0; i < jsoninstructions.length(); i++){
            try{ recipe.addInstruction(jsoninstructions.getJSONObject(i).getString("name"));}
            catch (Exception e){recipe.addInstruction("");}
        }

        return recipe;

    }

    public void deleteRecipe(long id){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        db.delete("recipesDB", "recipes.recipeid = " + id, null);
    }
}
