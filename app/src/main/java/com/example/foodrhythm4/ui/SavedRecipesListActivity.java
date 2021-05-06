package com.example.foodrhythm4.ui;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.ClipData;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.foodrhythm4.Constants;
import com.example.foodrhythm4.R;
import com.example.foodrhythm4.adapters.FirebaseRecipeListAdapter;
import com.example.foodrhythm4.adapters.FirebaseRecipeViewHolder;
import com.example.foodrhythm4.helper.ItemTouchHelperAdapter;
import com.example.foodrhythm4.helper.MyItemTouchHelperCallBack;
import com.example.foodrhythm4.helper.OnStartDragListener;
import com.example.foodrhythm4.models.Recipe;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

import butterknife.BindView;
import butterknife.ButterKnife;

public class SavedRecipesListActivity extends AppCompatActivity implements OnStartDragListener {
    private DatabaseReference mRecipeReference;
    private FirebaseRecyclerAdapter<Recipe, FirebaseRecipeViewHolder> mFirebaseAdapter;
    private ItemTouchHelper mItemTouchHelper;

    @BindView(R.id.recyclerView) RecyclerView mRecyclerView;
    @BindView(R.id.errorTextView) TextView mErrorTextView;
    @BindView(R.id.progressBar) ProgressBar mProgressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_saved_recipes_list);
        ButterKnife.bind(this);

        mRecipeReference = FirebaseDatabase.getInstance().getReference(Constants.FIREBASE_CHILD_RECIPES);

        setUpFirebaseAdapter();
        hideProgressBar();
        showRestaurants();
    }

    private void setUpFirebaseAdapter(){
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        String uid = user.getUid();

        Query query = FirebaseDatabase
                .getInstance()
                .getReference(Constants.FIREBASE_CHILD_RECIPES)
                .child(uid)
                .orderByChild(Constants.FIREBASE_QUERY_INDEX);


        FirebaseRecyclerOptions<Recipe> options =
                new FirebaseRecyclerOptions.Builder<Recipe>()
                        .setQuery(query, Recipe.class)
                        .build();

        mFirebaseAdapter = new FirebaseRecipeListAdapter(options, query, this, this);

        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.setAdapter(mFirebaseAdapter);
        mRecyclerView.setHasFixedSize(true);
        ItemTouchHelper.Callback callback = new MyItemTouchHelperCallBack((ItemTouchHelperAdapter) mFirebaseAdapter);
        mItemTouchHelper = new ItemTouchHelper(callback);
        mItemTouchHelper.attachToRecyclerView(mRecyclerView);
    }
    @Override
    protected void onStart() {
        super.onStart();
        mFirebaseAdapter.startListening();
    }

    @Override
    protected void onStop() {
        super.onStop();
        if(mFirebaseAdapter!= null) {
            mFirebaseAdapter.stopListening();
        }
    }

    private void showRestaurants() {
        mRecyclerView.setVisibility(View.VISIBLE);
    }

    private void hideProgressBar() {
        mProgressBar.setVisibility(View.GONE);
    }

    @Override
    public void onStartDrag(RecyclerView.ViewHolder viewHolder) {
        mItemTouchHelper.startDrag(viewHolder);
    }
}