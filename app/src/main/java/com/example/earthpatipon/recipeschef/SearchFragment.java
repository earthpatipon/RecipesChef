package com.example.earthpatipon.recipeschef;


import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.earthpatipon.recipeschef.utils.RecipeAdapter;
import com.example.earthpatipon.recipeschef.utils.RecipeCard;

import java.util.ArrayList;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class SearchFragment extends Fragment {

    private List<RecipeCard> recipeList;
    private Context context;
    private RecyclerView recyclerView;

    public SearchFragment() { }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = getActivity().getApplicationContext();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_recipe, container, false);
        LinearLayoutManager MyLayoutManager = new LinearLayoutManager(getActivity());
        MyLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        HomeActivity activity = (HomeActivity) getActivity();

        recyclerView = view.findViewById(R.id.cardView);
        recyclerView.setHasFixedSize(true);
        recipeList = activity.getRecipeList();
        if (recipeList.size() > 0 & recyclerView != null) {
            recyclerView.setAdapter(new RecipeAdapter(context, recipeList));
        }
        recyclerView.setLayoutManager(MyLayoutManager);
        return view;
    }

}