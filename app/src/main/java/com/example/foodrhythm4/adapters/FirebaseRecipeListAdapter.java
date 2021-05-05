package com.example.foodrhythm4.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;

import com.example.foodrhythm4.R;
import com.example.foodrhythm4.helper.ItemTouchHelperAdapter;
import com.example.foodrhythm4.helper.OnStartDragListener;
import com.example.foodrhythm4.models.Recipe;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DatabaseReference;

public class FirebaseRecipeListAdapter extends FirebaseRecyclerAdapter<Recipe,
        FirebaseRecipeViewHolder> implements ItemTouchHelperAdapter {

    private DatabaseReference mRef;
    private OnStartDragListener mOnStartDragListener;
    private Context mContext;

    public FirebaseRecipeListAdapter(FirebaseRecyclerOptions<Recipe> options, DatabaseReference ref,
                                     OnStartDragListener onStartDragListener,
                                     Context context) {
        super(options);
        mRef = ref.getRef();
        mOnStartDragListener = onStartDragListener;
        mContext = context;
    }

    @Override
    protected void onBindViewHolder(@NonNull FirebaseRecipeViewHolder firebaseRecipeViewHolder, int position, @NonNull Recipe recipe) {
        firebaseRecipeViewHolder.bindRecipe(recipe);

        firebaseRecipeViewHolder.mRecipeImageView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getActionMasked() == MotionEvent.ACTION_DOWN) {
                    mOnStartDragListener.onStartDrag(firebaseRecipeViewHolder);
                }
                return false;
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
        notifyItemMoved(fromPosition, toPosition);
        return false;
    }

    @Override
    public void onItemDismiss(int position) {
        getRef(position).removeValue();
    }
}
