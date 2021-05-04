package com.example.foodrhythm4;

import android.content.Context;
import android.widget.ArrayAdapter;

public class MyRecipesArrayAdapter extends ArrayAdapter {
    private Context mContext;
    private String[] mDessertRecipes;

    public MyRecipesArrayAdapter(Context mContext, int resource, String[] mDessertRecipes) {
        super(mContext, resource);
        this.mContext = mContext;
        this.mDessertRecipes = mDessertRecipes;
    }

    @Override
    public Object getItem(int position) {
        String dessertRecipes = mDessertRecipes[position];
        return String.format("%s", dessertRecipes);
    }

    @Override
    public int getCount() {
        return mDessertRecipes.length;
    }
}
