package com.example.foodrhythm4.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.foodrhythm4.Constants;
import com.example.foodrhythm4.R;
import com.example.foodrhythm4.models.Recipe;
import com.example.foodrhythm4.ui.RecipeDetailActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import org.parceler.Parcels;

import java.util.ArrayList;

public class FirebaseRecipeViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
    View mView;
    Context mContext;
    public ImageView mRecipeImageView;

    public FirebaseRecipeViewHolder(View itemView) {
        super(itemView);
        mView = itemView;
        mContext = itemView.getContext();
        itemView.setOnClickListener(this);
    }

    public void bindRecipe(Recipe recipe) {
        mRecipeImageView = (ImageView) mView.findViewById(R.id.imageUrlImageView);

        ImageView recipeImageView = (ImageView) mView.findViewById(R.id.imageUrlImageView);
        TextView nameTextView = (TextView) mView.findViewById(R.id.recipeNameTextView);
        TextView sourceUrlTextView = (TextView) mView.findViewById(R.id.sourceUrlTextView);
        TextView socialRankTextView = (TextView) mView.findViewById(R.id.socialRankTextView);

        Picasso.get().load(recipe.getImageUrl()).into(recipeImageView);

        nameTextView.setText(recipe.getTitle());
        sourceUrlTextView.setText(recipe.getSourceUrl());
        socialRankTextView.setText("Ranking: " + recipe.getSocialRank() + "/100");
    }

    @Override
    public void onClick(View view) {
        final ArrayList<Recipe> recipes = new ArrayList<>();

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        String uid = user.getUid();
        DatabaseReference ref = FirebaseDatabase
                .getInstance()
                .getReference(Constants.FIREBASE_CHILD_RECIPES)
                .child(uid);

        ref.addListenerForSingleValueEvent(new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    recipes.add(snapshot.getValue(Recipe.class));
                }

                int itemPosition = getLayoutPosition();

                Intent intent = new Intent(mContext, RecipeDetailActivity.class);
                intent.putExtra("position", itemPosition + "");
                intent.putExtra("recipes", Parcels.wrap(recipes));

                mContext.startActivity(intent);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
    }
}
