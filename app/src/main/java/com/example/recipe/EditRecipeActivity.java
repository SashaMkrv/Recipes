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
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.zip.Inflater;

import static android.R.attr.id;

public class EditRecipeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setTitle(R.string.editPageName);
        setContentView(R.layout.activity_edit_recipe);
        String[] egInst = {"1. mix everything", "2. into the oven", "3. consume"};
        String[] egIng = {"pumpkin", "flour", "tomato", "chicken stock"};

        LayoutInflater inflate = LayoutInflater.from(this);

        //set all those ingredients
        LinearLayout ingLayout = (LinearLayout) findViewById(R.id.ingredientList);
        ListHelper.addStringsToLinearLayout(egIng, ingLayout, inflate);

        //set all those instructions
        LinearLayout instLayout = (LinearLayout) findViewById(R.id.instructionList);
        ListHelper.addStringsToLinearLayout(egInst, instLayout, inflate);

    }
    public void onImageClick(View view){
        Intent intent = new Intent(this, ChangeImageActivity.class);
        startActivity(intent);
    }
}
