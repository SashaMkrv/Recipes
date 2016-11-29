package com.example.recipe;

//%% NEW FILE RecipeContainer BEGINS HERE %%

/*PLEASE DO NOT EDIT THIS CODE*/
/*This code was generated using the UMPLE 1.24.0-c37463a modeling language!*/


import java.util.*;

// line 2 "model.ump"
// line 11 "model.ump"
public class RecipeContainer
{

    //------------------------
    // MEMBER VARIABLES
    //------------------------

    //RecipeContainer Attributes
    private long recipeid;
    private String name;
    private String category;
    private String type;
    private List<String> ingredients;
    private List<String> instructions;

    //------------------------
    // CONSTRUCTOR
    //------------------------

    public RecipeContainer(long aRecipeid, String aName, String aCategory, String aType)
    {
        recipeid = aRecipeid;
        name = aName;
        category = aCategory;
        type = aType;
        ingredients = new ArrayList<String>();
        instructions = new ArrayList<String>();
    }

    //------------------------
    // INTERFACE
    //------------------------

    public boolean setRecipeid(long aRecipeid)
    {
        boolean wasSet = false;
        recipeid = aRecipeid;
        wasSet = true;
        return wasSet;
    }

    public boolean setName(String aName)
    {
        boolean wasSet = false;
        name = aName;
        wasSet = true;
        return wasSet;
    }

    public boolean setCategory(String aCategory)
    {
        boolean wasSet = false;
        category = aCategory;
        wasSet = true;
        return wasSet;
    }

    public boolean setType(String aType)
    {
        boolean wasSet = false;
        type = aType;
        wasSet = true;
        return wasSet;
    }

    public boolean addIngredient(String aIngredient)
    {
        boolean wasAdded = false;
        wasAdded = ingredients.add(aIngredient);
        return wasAdded;
    }

    public boolean removeIngredient(String aIngredient)
    {
        boolean wasRemoved = false;
        wasRemoved = ingredients.remove(aIngredient);
        return wasRemoved;
    }

    public boolean clearIngredients(){
        ingredients.clear();
        return true;
    }

    public boolean addInstruction(String aInstruction)
    {
        boolean wasAdded = false;
        wasAdded = instructions.add(aInstruction);
        return wasAdded;
    }

    public boolean removeInstruction(String aInstruction)
    {
        boolean wasRemoved = false;
        wasRemoved = instructions.remove(aInstruction);
        return wasRemoved;
    }

    public boolean clearInstructions(){
        instructions.clear();
        return true;
    }

    public long getRecipeid()
    {
        return recipeid;
    }

    public String getName()
    {
        return name;
    }

    public String getCategory()
    {
        return category;
    }

    public String getType()
    {
        return type;
    }

    public String getIngredient(int index)
    {
        String aIngredient = ingredients.get(index);
        return aIngredient;
    }

    public String[] getIngredients()
    {
        String[] newIngredients = ingredients.toArray(new String[ingredients.size()]);
        return newIngredients;
    }

    public int numberOfIngredients()
    {
        int number = ingredients.size();
        return number;
    }

    public boolean hasIngredients()
    {
        boolean has = ingredients.size() > 0;
        return has;
    }

    public int indexOfIngredient(String aIngredient)
    {
        int index = ingredients.indexOf(aIngredient);
        return index;
    }

    public String getInstruction(int index)
    {
        String aInstruction = instructions.get(index);
        return aInstruction;
    }

    public String[] getInstructions()
    {
        String[] newInstructions = instructions.toArray(new String[instructions.size()]);
        return newInstructions;
    }

    public int numberOfInstructions()
    {
        int number = instructions.size();
        return number;
    }

    public boolean hasInstructions()
    {
        boolean has = instructions.size() > 0;
        return has;
    }

    public int indexOfInstruction(String aInstruction)
    {
        int index = instructions.indexOf(aInstruction);
        return index;
    }

    public void delete()
    {}


    public String toString()
    {
        String outputString = "";
        return super.toString() + "["+
                "recipeid" + ":" + getRecipeid()+ "," +
                "name" + ":" + getName()+ "," +
                "category" + ":" + getCategory()+ "," +
                "type" + ":" + getType()+ "]"
                + outputString;
    }
}