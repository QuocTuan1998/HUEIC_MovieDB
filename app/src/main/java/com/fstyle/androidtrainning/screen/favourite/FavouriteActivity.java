package com.fstyle.androidtrainning.screen.favourite;

import android.content.Intent;
import android.content.res.Configuration;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;
import butterknife.BindView;
import butterknife.ButterKnife;
import com.fstyle.androidtrainning.R;
import com.fstyle.androidtrainning.adapter.MovieAdapter;
import com.fstyle.androidtrainning.listener.OnRecyclerViewItemListener;
import com.fstyle.androidtrainning.model.Movie;
import com.fstyle.androidtrainning.model.MovieUser;
import com.fstyle.androidtrainning.screen.detailmovie.DetailsMovieActivity;
import com.fstyle.androidtrainning.screen.signin.SignInActivity;
import com.fstyle.androidtrainning.util.Constant;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by ossierra on 12/27/17.
 */

public class FavouriteActivity extends Fragment
    implements OnRecyclerViewItemListener {
    private FirebaseAuth mFirebaseAuth;
    private FirebaseUser mFirebaseUser;
    private DatabaseReference mUserCloudEndPoint;
    private FirebaseDatabase mFirebaseInstance;
    private String mUserName;
    private String mUserEmail;
    private List<Movie> mFavouriteMovieList;
    @BindView(R.id.recycler_search)
    RecyclerView mRecyclerView;

    @Override
    public View onCreateView(LayoutInflater inflater,
            ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_favourite, container, false);
        ButterKnife.bind(this, view);
        mRecyclerView.setHasFixedSize(true);
        int columns;
        columns = getResources().getConfiguration().orientation == Configuration
                .ORIENTATION_LANDSCAPE ? 4 : 2;

        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(getActivity(), columns);
        mRecyclerView.setLayoutManager(layoutManager);
        // Initialize Firebase Auth
        mFirebaseAuth = FirebaseAuth.getInstance();
        mFirebaseUser = mFirebaseAuth.getCurrentUser();
        if (mFirebaseUser == null) {
            // Not signed in, launch the Sign In activity
            startActivity(new Intent(this.getActivity(), SignInActivity.class));
        } else {
            mUserName = mFirebaseUser.getDisplayName();
            mUserEmail = mFirebaseUser.getEmail();
            mFirebaseInstance = FirebaseDatabase.getInstance();
            mFavouriteMovieList = new ArrayList<>();
            // get reference to 'users' node
            mUserCloudEndPoint = mFirebaseInstance.getReference(Constant.USERS_NODE_FIREBASE);
            mUserCloudEndPoint.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    for (DataSnapshot categorySnapshot : dataSnapshot.getChildren()) {
                        MovieUser user = categorySnapshot.getValue(MovieUser.class);
                        if (user.getEmail().equals(mUserEmail)) {
                            if (user.getFavouriteMovies() != null) {
                                List<HashMap<String, String>> favouriteList = new ArrayList<>();
                                favouriteList = user.getFavouriteMovies();
                                for (int i = 0; i < favouriteList.size(); i++) {
                                    Movie movie = new Movie();
                                    movie.setId(Integer.valueOf(favouriteList.get(i).get("id")));
                                    movie.setOriginalTitle(favouriteList.get(i).get(
                                            "original_title"));
                                    movie.setPosterPath(favouriteList.get(i).get("poster_url"));
                                    mFavouriteMovieList.add(movie);
                                }
                            }
                            break;
                        }
                    }
                    //show favourite movies of the current user
                    showFavouriteMovies(mFavouriteMovieList);
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
        }
        return view;
    }
    private void showFavouriteMovies(List<Movie> mMovieList) {
        if (mMovieList.isEmpty()) {
            Toast.makeText(this.getContext(), "No Result Found", Toast.LENGTH_SHORT).show();
        } else {
            MovieAdapter movieAdapter = new MovieAdapter(mMovieList, R.layout.item_movie);
            movieAdapter.setOnRecyclerViewItemListener(this);
            mRecyclerView.setAdapter(movieAdapter);
        }
    }

    @Override
    public void onItemClick(Movie movie) {
        Intent intent = new Intent(getActivity(), DetailsMovieActivity.class);
        intent.putExtra(Constant.EXTRA_MOVIE_ID, movie.getId());
        startActivity(intent);
    }
}

