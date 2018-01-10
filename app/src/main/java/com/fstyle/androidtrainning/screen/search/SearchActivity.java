package com.fstyle.androidtrainning.screen.search;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Toast;
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
        implements CallAPIListener, OnRecyclerViewItemListener, SearchView.OnQueryTextListener {
    Unbinder unbinder;
    @BindView(R.id.progress_bar)
    ProgressBar progressBar;
    @BindView(R.id.tool_bar_search)
    Toolbar mToolbar;
    @BindView(R.id.linear_search_not_found)
    LinearLayout mLinearLayout;
    @BindView(R.id.recycler_search)
    RecyclerView mRecyclerView;

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
            mLinearLayout.setVisibility(View.GONE);
            mRecyclerView.setVisibility(View.VISIBLE);

            showMovieOnGrid(mMovieList);
        }
    }

    @Override
    public void onCallAPIError(Exception e) {

    }

    @Override
    public void onItemClick(Movie movie) {
        Intent intent = new Intent(getActivity(), DetailsMovieActivity.class);
        intent.putExtra(Constant.EXTRA_MOVIE_ID, movie.getId());
        startActivity(intent);
    }

    private void initLayoutReferences() {
        ((AppCompatActivity) getActivity()).setSupportActionBar(mToolbar);
        setHasOptionsMenu(true);
        mToolbar.setTitle(Constant.TITLE_TOOLBAR);

        mRecyclerView.setHasFixedSize(true);
        int columns;
        columns = getResources().getConfiguration().orientation == Configuration
                .ORIENTATION_LANDSCAPE ? 4 : 2;

        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(getActivity(), columns);
        mRecyclerView.setLayoutManager(layoutManager);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.menu_items, menu);
        MenuItem menuItem = menu.findItem(R.id.action_search);
        SearchView searchMovieView = (SearchView) menuItem.getActionView();
        searchMovieView.setOnQueryTextListener(this);
        menuItem.expandActionView();
    }

    private void showMovieOnGrid(List<Movie> mMovieList) {

        if (mMovieList.isEmpty()) {
            Toast.makeText(this.getContext(), "No Result Found", Toast.LENGTH_SHORT).show();
        } else {
            MovieAdapter movieAdapter = new MovieAdapter(mMovieList, R.layout.item_movie);
            movieAdapter.setOnRecyclerViewItemListener(this);
            mRecyclerView.setAdapter(movieAdapter);
        }
    }

    @Override
    public boolean onQueryTextSubmit(String s) {
        return true;
    }

    @Override
    public boolean onQueryTextChange(String keyword) {
        if (keyword == null || keyword.isEmpty()) {
            return false;
        }

        new GetMoviesAsynTask(SearchActivity.this).execute(
                "https://api.themoviedb.org/3/search/movie?query=" + keyword + "&api_key="
                        + Constant.API_KEY + "&language=" + Constant.LANGUAGE + "&page="
                        + Constant.DEFAULT_PAGE
        );
        return true;
    }
}
