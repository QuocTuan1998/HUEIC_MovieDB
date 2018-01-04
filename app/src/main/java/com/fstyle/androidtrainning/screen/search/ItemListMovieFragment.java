package com.fstyle.androidtrainning.screen.search;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.fstyle.androidtrainning.R;
import com.fstyle.androidtrainning.adapter.MovieAdapter;
import com.fstyle.androidtrainning.model.Movie;
import java.util.List;

public class ItemListMovieFragment extends Fragment {
    private List<Movie> mMovieList;
    private RecyclerView mRecyclerView;
    private MovieAdapter mMovieSearchAdapter;

    public ItemListMovieFragment() {
        // Required empty public constructor
    }

    // TODO: Rename and change types and number of parameters
    public static ItemListMovieFragment newInstance() {
        ItemListMovieFragment fragment = new ItemListMovieFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater,
            ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_item_list_movie, container,
                false);
        Context context = view.getContext();
        mRecyclerView = (RecyclerView) view.findViewById(R.id.recycler_view);
        mRecyclerView.setLayoutManager(new GridLayoutManager(context, 2));
        mRecyclerView.setAdapter(mMovieSearchAdapter);

        return view;
    }

}
