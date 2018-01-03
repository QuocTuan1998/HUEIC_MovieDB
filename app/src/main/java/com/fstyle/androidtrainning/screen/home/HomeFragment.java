package com.fstyle.androidtrainning.screen.home;

import android.content.Intent;
import android.content.res.Configuration;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import butterknife.BindView;
import butterknife.ButterKnife;
import com.fstyle.androidtrainning.R;
import com.fstyle.androidtrainning.adapter.MovieAdapter;
import com.fstyle.androidtrainning.listener.CallAPIListener;
import com.fstyle.androidtrainning.listener.OnRecyclerViewItemListener;
import com.fstyle.androidtrainning.model.Movie;
import com.fstyle.androidtrainning.restapi.GetMoviesAsynTask;
import com.fstyle.androidtrainning.screen.detailmovie.DetailsMovieActivity;
import com.fstyle.androidtrainning.util.Constant;
import java.util.List;

/**
 * Created by ossierra on 12/27/17.
 */

public class HomeFragment extends Fragment implements CallAPIListener, OnRecyclerViewItemListener {

    private static final String TAG = HomeFragment.class.getName();

    @BindView(R.id.progress_bar)
    ProgressBar progressBar;

    @BindView(R.id.recycler_movies)
    RecyclerView recyclerView;

    @Override
    public View onCreateView(LayoutInflater inflater,
            ViewGroup container, Bundle savedInstanceState) {
        View viewContext = inflater.inflate(R.layout.fragment_home, container, false);
        ButterKnife.bind(this, viewContext);
        initLayoutReferences();
        new GetMoviesAsynTask(HomeFragment.this).execute();
        return viewContext;
    }

    @Override
    public void onStartCallAPI() {
        progressBar.setVisibility(View.VISIBLE);
    }

    @Override
    public void onCallAPISuccess(List mMovieList) {
        progressBar.setVisibility(View.GONE);
        if (mMovieList != null) {
            showMovieOnGrid(mMovieList);
        }
    }

    @Override
    public void onCallAPIError(Exception e) {
        Log.e(TAG, e.getMessage());
    }

    @Override
    public void onItemClick(Movie movie) {
        Intent intent = new Intent(getActivity(), DetailsMovieActivity.class);
        intent.putExtra(Constant.EXTRA_MOVIE_ID, movie);
        startActivity(intent);
    }

    private void initLayoutReferences() {
        recyclerView.setHasFixedSize(true);

        int columns;
        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
            columns = getResources().getInteger(R.integer.no_of_columns);
        } else {
            columns = getResources().getInteger(R.integer.no_default_of_columns);
        }
        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(getActivity(), columns);
        recyclerView.setLayoutManager(layoutManager);
    }

    private void showMovieOnGrid(List<Movie> mMovieList) {
        MovieAdapter adapter = new MovieAdapter(mMovieList, R.layout.item_movie);
        recyclerView.setAdapter(adapter);
        adapter.setOnRecyclerViewItemListener(this);
    }
}
