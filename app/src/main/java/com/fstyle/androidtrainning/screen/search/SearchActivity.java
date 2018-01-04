package com.fstyle.androidtrainning.screen.search;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import com.fstyle.androidtrainning.R;
import com.fstyle.androidtrainning.adapter.MovieAdapter;
import com.fstyle.androidtrainning.listener.CallAPIListener;
import com.fstyle.androidtrainning.model.Movie;
import com.fstyle.androidtrainning.restapi.GetMoviesAsynTask;
import com.fstyle.androidtrainning.listener.OnRecyclerViewItemListener;
import com.fstyle.androidtrainning.screen.detailmovie.DetailsMovieActivity;
import com.fstyle.androidtrainning.util.Constant;
import java.util.List;

public class SearchActivity extends Fragment
        implements CallAPIListener, OnRecyclerViewItemListener {
    private MovieAdapter mMovieAdapter;
    Unbinder unbinder;
    @BindView(R.id.textSearchMovie)
    EditText mEditTextSearchMovie;
    @BindView(R.id.imageSearchMovie)
    ImageView mImageSearch;
    @BindView(R.id.progress_bar)
    ProgressBar progressBar;
    @BindView(R.id.recycler_view)
    RecyclerView recyclerView;

    private static final String TAG = SearchActivity.class.getName();

    public static SearchActivity newInstance() {
        SearchActivity fragment = new SearchActivity();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater,
            ViewGroup container, Bundle savedInstanceState) {
        View viewContext = inflater.inflate(R.layout.fragment_search, container, false);
        unbinder = ButterKnife.bind(this, viewContext);
        initLayoutReferences();
        new GetMoviesAsynTask(SearchActivity.this).execute();
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

    }

    @Override
    public void onItemClick(Movie movie) {
        Intent intent = new Intent(getActivity(), DetailsMovieActivity.class);
        intent.putExtra(Constant.EXTRA_MOVIE_ID, movie);
        startActivity(intent);
    }

    private void initLayoutReferences() {
        mImageSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String keyword = mEditTextSearchMovie.getText().toString();
                if (keyword.isEmpty()) {
                    return;
                }
                new GetMoviesAsynTask(SearchActivity.this).execute(
                        "http://api.themoviedb.org/3/search/movie?Title=" + keyword
                );
            }
        });
        recyclerView.setHasFixedSize(true);

        int columns;
        columns = getResources().getConfiguration().orientation == Configuration
                .ORIENTATION_LANDSCAPE ? 4 : 2;

        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(getActivity(), columns);
        recyclerView.setLayoutManager(layoutManager);
    }

    private void showMovieOnGrid(List<Movie> mMovieList) {
        mMovieAdapter = new MovieAdapter(mMovieList, R.layout.item_movie);
        mMovieAdapter.setOnRecyclerViewItemListener(this);
        recyclerView.setAdapter(mMovieAdapter);
    }
}
