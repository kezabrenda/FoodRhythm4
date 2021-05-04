package com.example.foodrhythm4.models;

import java.util.List;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import org.parceler.Parcel;

@Parcel
public class ForkifySearchResponse {

    @SerializedName("count")
    @Expose
    private Integer count;
    @SerializedName("recipes")
    @Expose
    private List<Recipe> recipes = null;

    /**
     * No args constructor for use in serialization
     * 
     */
    public ForkifySearchResponse() {
    }

    /**
     * 
     * @param recipes
     * @param count
     */
    public ForkifySearchResponse(Integer count, List<Recipe> recipes) {
        super();
        this.count = count;
        this.recipes = recipes;
    }

    public Integer getCount() {
        return count;
    }

    public void setCount(Integer count) {
        this.count = count;
    }

    public List<Recipe> getRecipes() {
        return recipes;
    }

    public void setRecipes(List<Recipe> recipes) {
        this.recipes = recipes;
    }

}
