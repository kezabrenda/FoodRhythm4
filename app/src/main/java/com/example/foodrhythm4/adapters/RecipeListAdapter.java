package com.example.foodrhythm4.adapters;

import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import com.example.foodrhythm4.Constants;
import com.example.foodrhythm4.R;
import com.example.foodrhythm4.models.Recipe;
import com.example.foodrhythm4.ui.RecipeDetailActivity;
import com.example.foodrhythm4.ui.RecipeDetailFragment;
import com.squareup.picasso.Picasso;

import org.parceler.Parcels;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class RecipeListAdapter extends RecyclerView.Adapter<RecipeListAdapter.RecipesViewHolder> {
    private List<Recipe> mRecipes;
    private Context mContext;

    public RecipeListAdapter(Context context, List<Recipe> recipes) {
        mContext = context;
        mRecipes = recipes;
    }

    @Override
    public RecipesViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recipe_list_item, parent, false);
        RecipesViewHolder viewHolder = new RecipesViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(RecipesViewHolder holder, int position) {
        holder.bindRecipe(mRecipes.get(position));
    }

    @Override
    public int getItemCount() {
        return mRecipes.size();
    }

    public class RecipesViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        @BindView(R.id.imageUrlImageView) ImageView mImageUrlImageView;
        @BindView(R.id.recipeNameTextView) TextView mNameTextView;
        @BindView(R.id.sourceUrlTextView) TextView mSourceUrlTextView;
        @BindView(R.id.socialRankTextView) TextView mSocialRankTextView;

        private int mOrientation;

        private Context mContext;

        public RecipesViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            mContext = itemView.getContext();
            itemView.setOnClickListener(this);

            // Determines the current orientation of the device:
            mOrientation = itemView.getResources().getConfiguration().orientation;

            // Checks if the recorded orientation matches Android's landscape configuration.
            // if so, we create a new DetailFragment to display in our special landscape layout:
            if (mOrientation == Configuration.ORIENTATION_LANDSCAPE) {
                createDetailFragment(0);
            }
        }

        private void createDetailFragment(int position) {
            // Creates new RestaurantDetailFragment with the given position:
            RecipeDetailFragment detailFragment = RecipeDetailFragment.newInstance(mRecipes, position);
            // Gathers necessary components to replace the FrameLayout in the layout with the RestaurantDetailFragment:
            FragmentTransaction ft = ((FragmentActivity) mContext).getSupportFragmentManager().beginTransaction();
            //  Replaces the FrameLayout with the RestaurantDetailFragment:
            ft.replace(R.id.recipeDetailContainer, detailFragment);
            // Commits these changes:
            ft.commit();
        }

        public void bindRecipe(Recipe recipe) {
            Picasso.get().load(recipe.getImageUrl()).into(mImageUrlImageView);
            mNameTextView.setText(recipe.getTitle());
            mSourceUrlTextView.setText(recipe.getSourceUrl());
            mSocialRankTextView.setText("social_rank: " + recipe.getSocialRank());
        }

        @Override
        public void onClick(View v) {
            int itemPosition = getLayoutPosition();
            if(mOrientation == Configuration.ORIENTATION_LANDSCAPE){
                createDetailFragment(itemPosition);
            } else {
                Intent intent = new Intent(mContext, RecipeDetailActivity.class);
                intent.putExtra(Constants.EXTRA_KEY_POSITION, itemPosition);
                intent.putExtra(Constants.EXTRA_KEY_RECIPES, Parcels.wrap(mRecipes));
                mContext.startActivity(intent);
            }
        }
    }
}
