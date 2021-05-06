package com.example.foodrhythm4.adapters;

import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentTransaction;

import com.example.foodrhythm4.Constants;
import com.example.foodrhythm4.R;
import com.example.foodrhythm4.helper.ItemTouchHelperAdapter;
import com.example.foodrhythm4.helper.OnStartDragListener;
import com.example.foodrhythm4.models.Recipe;
import com.example.foodrhythm4.ui.RecipeDetailActivity;
import com.example.foodrhythm4.ui.RecipeDetailFragment;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Query;

import org.parceler.Parcels;

import java.util.ArrayList;
import java.util.Collections;

public class FirebaseRecipeListAdapter extends FirebaseRecyclerAdapter<Recipe,
        FirebaseRecipeViewHolder> implements ItemTouchHelperAdapter {

    private DatabaseReference mRef;
    private OnStartDragListener mOnStartDragListener;
    private Context mContext;
    private ChildEventListener mChildEventListener;
    private ArrayList<Recipe> mRecipes = new ArrayList<>();

    private int mOrientation;

    public FirebaseRecipeListAdapter(FirebaseRecyclerOptions<Recipe> options, Query ref,
                                     OnStartDragListener onStartDragListener,
                                     Context context) {
        super(options);
        mRef = ref.getRef();
        mOnStartDragListener = onStartDragListener;
        mContext = context;

        mChildEventListener = mRef.addChildEventListener(new ChildEventListener() {

            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                mRecipes.add(dataSnapshot.getValue(Recipe.class));
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    @Override
    protected void onBindViewHolder(@NonNull FirebaseRecipeViewHolder firebaseRecipeViewHolder, int position, @NonNull Recipe recipe) {
        firebaseRecipeViewHolder.bindRecipe(recipe);

        mOrientation = firebaseRecipeViewHolder.itemView.getResources().getConfiguration().orientation;
        if (mOrientation == Configuration.ORIENTATION_LANDSCAPE) {
            createDetailFragment(0);
        }

        firebaseRecipeViewHolder.mRecipeImageView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getActionMasked() == MotionEvent.ACTION_DOWN) {
                    mOnStartDragListener.onStartDrag(firebaseRecipeViewHolder);
                }
                return false;
            }
        });

        firebaseRecipeViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int itemPosition = firebaseRecipeViewHolder.getAdapterPosition();
                if (mOrientation == Configuration.ORIENTATION_LANDSCAPE) {
                    createDetailFragment(itemPosition);
                } else {
                    Intent intent = new Intent(mContext, RecipeDetailActivity.class);
                    intent.putExtra(Constants.EXTRA_KEY_POSITION, itemPosition);
                    intent.putExtra(Constants.EXTRA_KEY_RECIPES, Parcels.wrap(mRecipes));
                    mContext.startActivity(intent);
                }
            }
        });
    }

    @NonNull
    @Override
    public FirebaseRecipeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recipe_list_item_drag, parent, false);
        return new FirebaseRecipeViewHolder(view);
    }

    @Override
    public boolean onItemMove(int fromPosition, int toPosition) {
        Collections.swap(mRecipes, fromPosition, toPosition);
        notifyItemMoved(fromPosition, toPosition);
        setIndexInFirebase();
        return false;
    }

    @Override
    public void onItemDismiss(int position) {
        mRecipes.remove(position);
        getRef(position).removeValue();
    }

    private void setIndexInFirebase() {
        for (Recipe recipe : mRecipes) {
            int index = mRecipes.indexOf(recipe);
            DatabaseReference ref = getRef(index);
            recipe.setIndex(Integer.toString(index));
            ref.setValue(recipe);
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
    @Override
    public void stopListening() {
        super.stopListening();
        mRef.removeEventListener(mChildEventListener);
    }
}
