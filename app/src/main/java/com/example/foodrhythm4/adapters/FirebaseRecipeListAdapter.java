package com.example.foodrhythm4.adapters;

import android.view.ViewGroup;

import androidx.annotation.NonNull;

import com.example.foodrhythm4.helper.ItemTouchHelperAdapter;
import com.example.foodrhythm4.models.Recipe;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;

public class FirebaseRecipeListAdapter extends FirebaseRecyclerAdapter<Recipe,
        FirebaseRecipeViewHolder> implements ItemTouchHelperAdapter {

    /**
     * Initialize a {@link RecyclerView.Adapter} that listens to a Firebase query. See
     * {@link FirebaseRecyclerOptions} for configuration options.
     *
     * @param options
     */
    public FirebaseRecipeListAdapter(@NonNull FirebaseRecyclerOptions<Recipe> options) {
        super(options);
    }

    @Override
    public boolean onItemMove(int fromPosition, int toPosition) {
        return false;
    }

    @Override
    public void onItemDismiss(int position) {

    }

    @Override
    protected void onBindViewHolder(@NonNull FirebaseRecipeViewHolder holder, int position, @NonNull Recipe model) {

    }

    @NonNull
    @Override
    public FirebaseRecipeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return null;
    }
}
