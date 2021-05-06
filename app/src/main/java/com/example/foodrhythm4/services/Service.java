package com.example.foodrhythm4.services;

import com.example.foodrhythm4.Constants;
import com.example.foodrhythm4.models.Recipe;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class Service {
    public void findRecipes(String location, Callback callback){
        OkHttpClient client = new OkHttpClient.Builder()
                .build();

        HttpUrl.Builder urlBuilder = HttpUrl.parse(Constants.Forkify_BASE_URL).newBuilder();
        String url = urlBuilder.build().toString();

        Request request = new Request.Builder()
                .url(url)
                .build();

        Call call = client.newCall(request);
        call.enqueue(callback);
    }

    public ArrayList<Recipe> processResults(Response response){
        ArrayList<Recipe> recipes = new ArrayList<>();
        try{
            String jsonData = response.body().string();
            JSONObject forkifyJSON = new JSONObject(jsonData);
            JSONArray recipesJSON = forkifyJSON.getJSONArray("recipes");
            if (response.isSuccessful()){
                for (int i = 0; i < recipesJSON.length(); i++){
                    JSONObject recipeJSON = recipesJSON.getJSONObject(i);
                    String title = recipeJSON.getString("title");
                    String publisher = recipeJSON.optString("display_publisher", "publisher not available");
                    String sourceUrl = recipeJSON.getString("sourceUrl");
                    double socialRank = recipeJSON.getDouble("socialRank");
                    String imageUrl = recipeJSON.getString("image_url");
                    String recipeId = recipeJSON.getString("recipeId");
                    String publisherUrl = recipeJSON.optString("display_publisherUrl", "publisherUrl not available");
                    Recipe recipe = new Recipe( publisher, title,  sourceUrl,  recipeId,
                            imageUrl,  socialRank,  publisherUrl);
                    recipes.add(recipe);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return recipes;
    }
}
