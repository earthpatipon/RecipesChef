/* Group: Aoong Aoong
 * Members: Tanaporn 5888124, Kanjanaporn 5888178, Patipon 5888218
 */
package com.example.earthpatipon.recipeschef.entity;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

@Entity
public class Recipe {

    @PrimaryKey (autoGenerate = true)
    @ColumnInfo (name = "RecipeID")
    private int recipeID;
    @ColumnInfo (name = "RecipeName")
    private String recipeName;
    @ColumnInfo (name = "Description")
    private String description;
    @ColumnInfo (name = "Difficulty")
    private String difficulty;
    @ColumnInfo (name = "Time")
    private int time;
    @ColumnInfo (name = "Serve")
    private int serve;
    @ColumnInfo (name = "Ingredient")
    private String ingredient;
    @ColumnInfo (name = "Instruction")
    private String instruction;
    @ColumnInfo (name = "Category")
    private String category;
    @ColumnInfo (name = "Image")
    private String image;

    /**
     * Constructor
     */
    public Recipe(String recipeName, String description, String difficulty,
                  int time, int serve, String ingredient,
                  String instruction, String category, String image) {
        this.recipeName = recipeName;
        this.description = description;
        this.difficulty = difficulty;
        this.time = time;
        this.serve = serve;
        this.ingredient = ingredient;
        this.instruction = instruction;
        this.category = category;
        this.image = image; // keep reference
    }

    /**
     * Getters and Setters
     */
    public int getRecipeID() {
        return recipeID;
    }

    public void setRecipeID(int recipeID) {
        this.recipeID = recipeID;
    }

    public String getRecipeName() {
        return recipeName;
    }

    public void setRecipeName(String recipeName) {
        this.recipeName = recipeName;
    }

    public String getImage() {
        return image;
    }

    public void setPicture(String image) { this.image = image;}

    public String getDescription() { return description; }

    public void setDescription(String description) { this.description = description; }

    public String getDifficulty() { return difficulty; }

    public void setDifficulty(String difficulty) { this.difficulty = difficulty; }

    public int getTime() { return time; }

    public void setTime(int time) { this.time = time; }

    public int getServe() { return serve; }

    public void setServe(int serve) { this.serve = serve; }

    public String getIngredient() { return ingredient; }

    public void setIngredient(String ingredient) { this.ingredient = ingredient; }

    public String getInstruction() { return instruction; }

    public void setInstruction(String instruction) { this.instruction = instruction; }

    public String getCategory() { return category; }

    public void setCategory(String category) { this.category = category; }
}
