package com.example.recipe;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

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
                        "recipeCategory varchar(20));" +
                "create table ingredients" +
                "(ingredientname varchar(20)," +
                "recipeid integer," +
                "PRIMARY KEY(ingredientName, recipeid),"+
                "FOREIGN KEY(recipeid) REFERENCES recipes(recipeid);");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    public void saveRecipe(String name, String type, String category,
                           String[] instructions, String[] ingredients){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put("recipeName", name);
        cv.put("recipeType", type);
        cv.put("recipeCategory", category);
        long id = db.insert("recipesDB", null, cv);
        for(String ing: ingredients){
            cv = new ContentValues();
            cv.put("recipeid", id);
            cv.put("ingredientName", ing);
            db.insert("recipesDB", null, cv);
        }
    }
}
