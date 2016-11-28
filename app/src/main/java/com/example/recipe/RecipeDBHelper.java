package com.example.recipe;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import org.json.JSONArray;

import java.util.Arrays;

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
                "FOREIGN KEY(recipeid) REFERENCES recipes(recipeid);");
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

    public void getRecipe(long id){
        SQLiteDatabase db = this.getWritableDatabase();

    }
}
