package com.fstyle.androidtrainning.screen.detailmovie;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.bumptech.glide.Glide;
import com.fstyle.androidtrainning.R;
import com.fstyle.androidtrainning.listener.CallAPIListener;
import com.fstyle.androidtrainning.model.Movie;
import com.fstyle.androidtrainning.model.MovieUser;
import com.fstyle.androidtrainning.model.Trailer;
import com.fstyle.androidtrainning.restapi.GetMovieAsynTask;
import com.fstyle.androidtrainning.restapi.GetTrailerMovieAsynTank;
import com.fstyle.androidtrainning.util.Constant;
import com.fstyle.androidtrainning.util.DateTimeUtils;
import com.fstyle.androidtrainning.util.StringUtils;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerFragment;
import com.google.android.youtube.player.YouTubeThumbnailLoader;
import com.google.android.youtube.player.YouTubeThumbnailView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

/**
 * Created by ossierra on 1/2/18.
 */

public class DetailsMovieActivity extends AppCompatActivity
        implements CallAPIListener, YouTubePlayer.OnInitializedListener,
        YouTubeThumbnailView.OnInitializedListener, View.OnClickListener,
        GoogleApiClient.OnConnectionFailedListener{
    private ImageView mImageFavorite, mImageBigView, mImageSmallView;
    private TextView mTextTitleMovie, mTextPublishTime, mTextTimeMovie;
    private TextView mTextKindMovie, mTextRate, mTextOverview;
    private YouTubeThumbnailView mThumbnailView;
    private YouTubePlayerFragment mYouTubePlayer;
    private String mYoutubeKey;
    private YouTubePlayer.OnInitializedListener mOnPlayerInitListener;
    private RelativeLayout mRelativeLayout;
    private static final String TAG = "DetailsMovieActivity";

    private FirebaseAuth mFirebaseAuth;
    private FirebaseUser mFirebaseUser;
    private DatabaseReference mUserCloudEndPoint;
    private FirebaseDatabase mFirebaseInstance;
    private GoogleApiClient mGoogleApiClient;

    private String urlPoster, titleMovie;
    private Integer movieId;
    private String mUserId;
    private String mUserName;
    private String mUserEmail;
    private MovieUser mUser;
    private String userId;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_details);

        movieId = getIntent().getIntExtra(Constant.EXTRA_MOVIE_ID, 0);

        new GetMovieAsynTask(DetailsMovieActivity.this).execute(
                "https://api.themoviedb.org/3/movie/" + movieId + "?api_key="
                        + Constant.API_KEY + "&language=" + Constant.LANGUAGE
        );
        new GetTrailerMovieAsynTank(DetailsMovieActivity.this).execute(
                "https://api.themoviedb.org/3/movie/" + movieId
                        + "/videos?api_key=" + Constant.API_KEY
        );

        initViews();

    }

    private void initViews() {
        mImageBigView = findViewById(R.id.imageBigView);
        mImageSmallView = findViewById(R.id.imageSmallView);
        mTextTitleMovie = findViewById(R.id.textTitleMovie);
        mTextPublishTime = findViewById(R.id.textPublishTime);
        mTextTimeMovie = findViewById(R.id.textTimeMovie);
        mTextKindMovie = findViewById(R.id.textKindMovie);
        mTextRate = findViewById(R.id.textRate);
        mTextOverview = findViewById(R.id.text_overview);
        mImageFavorite = findViewById(R.id.imageFavorite);

        mRelativeLayout = findViewById(R.id.relative_play);
        mRelativeLayout.setOnClickListener(this);
        mThumbnailView = findViewById(R.id.youtube_thumnail);
        mYouTubePlayer = YouTubePlayerFragment.newInstance();
        mYouTubePlayer.initialize(Constant.GOOGLE_API_KEY, mOnPlayerInitListener);
        getFragmentManager().beginTransaction()
                .replace(R.id.frame_fragment, mYouTubePlayer)
                .commit();
    }

    private void fillData(Movie movie) {
        if (movie != null) {
            if (movie.getBackdropPath() != null) {
                String urlBackdrop = StringUtils.convertPosterPathToUrlPoster(movie
                        .getBackdropPath());
                Glide.with(this).load(urlBackdrop).into(mImageBigView);
            }
            if (movie.getPosterPath() != null) {
                urlPoster = StringUtils.convertPosterPathToUrlPoster(movie.getPosterPath());
                Glide.with(this).load(urlPoster).into(mImageSmallView);
            }
            String genresCommaSeparated = StringUtils
                    .convertListToStringCommaSeparated(movie.getMovieGenres());
            String rateMovie = movie.getVoteAverage() + Constant.MAX_POINT;
            String runTimeMovie = movie.getRuntime() + Constant.MINUTE;
            Date date = DateTimeUtils.convertStringToDate(movie.getReleaseDate(),
                    Constant.DATE_FORMAT_DD_MM_YYYY);
            String releaseDate = DateTimeUtils.getStrDateTimeFormatted(date,
                    Constant.DATE_FORMAT_DD_MMM_YYYY);

            titleMovie = movie.getTitle();
            mTextTitleMovie.setText(movie.getTitle());
            mTextPublishTime.setText(releaseDate);
            mTextTimeMovie.setText(runTimeMovie);
            mTextKindMovie.setText(genresCommaSeparated);
            mTextRate.setText(rateMovie);
            mTextOverview.setText(movie.getOverview());
        }
    }

    @Override
    public void onStartCallAPI() {

    }

    @Override
    public void onCallAPISuccess(List mList) {
        showButtonFavourite();
        mImageFavorite.setOnClickListener(this);

        if (mList != null && mList.size() > 0) {
            mThumbnailView.setVisibility(View.VISIBLE);
            if (mList instanceof List) {
                if (((List<Movie>) mList).get(0) instanceof Movie) {
                    List<Movie> movies = mList;
                    for (Movie movie : movies) {
                        fillData(movie);
                    }
                } else {
                    if (((List<Trailer>) mList).get(0) instanceof Trailer) {
                        List<Trailer> trailers = mList;
                        showMovieOnGrid(trailers);
                    }
                }
            }
        } else {
            mRelativeLayout.setVisibility(View.INVISIBLE);
            mRelativeLayout.setVisibility(View.GONE);
        }
    }

    @Override
    public void onCallAPIError(Exception e) {

    }

    @Override
    public void onInitializationSuccess(YouTubePlayer.Provider provider,
            YouTubePlayer youTubePlayer, boolean b) {
        mThumbnailView.setVisibility(View.GONE);
        youTubePlayer.loadVideo(mYoutubeKey);
        youTubePlayer.setShowFullscreenButton(false);
    }

    @Override
    public void onInitializationFailure(YouTubePlayer.Provider provider,
            YouTubeInitializationResult youTubeInitializationResult) {
        Log.e(TAG, "onInitializationFailure: ");
    }

    @Override
    public void onInitializationSuccess(YouTubeThumbnailView youTubeThumbnailView,
            final YouTubeThumbnailLoader youTubeThumbnailLoader) {
        youTubeThumbnailLoader.setVideo(mYoutubeKey);
        youTubeThumbnailLoader.setOnThumbnailLoadedListener(new YouTubeThumbnailLoader
                .OnThumbnailLoadedListener() {
            @Override
            public void onThumbnailLoaded(YouTubeThumbnailView youTubeThumbnailView, String s) {
                youTubeThumbnailLoader.release();
            }

            @Override
            public void onThumbnailError(YouTubeThumbnailView youTubeThumbnailView,
                    YouTubeThumbnailLoader.ErrorReason errorReason) {
                Log.e(TAG, "onThumbnailError: ");
            }
        });
    }

    @Override
    public void onInitializationFailure(YouTubeThumbnailView youTubeThumbnailView,
            YouTubeInitializationResult youTubeInitializationResult) {
        Log.e(TAG, "onInitializationFailure: ");
    }

    private void showMovieOnGrid(List<Trailer> mListTrailer) {

        if (mListTrailer.isEmpty()) {
            Toast.makeText(this, "No Result Found", Toast.LENGTH_SHORT).show();
        } else {
            if (mListTrailer != null) {
                for (Trailer trailer : mListTrailer) {
                    if (trailer.getName().contains(Constant.OFFICIAL)) {
                        mYoutubeKey = trailer.getKey();
                        break;
                    } else {
                        mYoutubeKey = mListTrailer.get(Constant.FIRST_TRAILER).getKey();
                    }
                }
                mThumbnailView.initialize(Constant.GOOGLE_API_KEY, this);
            }
        }
    }

    private void createUser(String name, String email, Integer movieID,
            String originalTitleMovie, String posterUrl) {
        if (TextUtils.isEmpty(mUserId)) {
            mUserId = mUserCloudEndPoint.push().getKey();
        }
        MovieUser user = new MovieUser();
        user.setEmail(email);
        user.setName(name);
        ArrayList<HashMap<String, String>> favouriteMoVie =
                new ArrayList<HashMap<String, String>>();
        HashMap<String, String> movie = new HashMap<String, String>();
        movie = getMovie(movieID.toString(), originalTitleMovie, posterUrl);

        favouriteMoVie.add(movie);
        user.setFavouriteMovies(favouriteMoVie);
        mUserCloudEndPoint.child(mUserId).setValue(user);
    }

    private void addMovie(Integer movieID, String originalTitleMovie,
            String posterUrl, Integer key) {
        HashMap<String, String> movie = new HashMap<String, String>();
        movie = getMovie(movieID.toString(), originalTitleMovie, posterUrl);

        mUserCloudEndPoint.child(userId).child("favouriteMovies").child(key.toString())
                .setValue(movie);
    }

    private  HashMap<String, String> getMovie(String id, String title, String url) {
        HashMap<String, String> movie = new HashMap<String, String>();
        movie.put("id", id);
        movie.put("original_title", title);
        movie.put("poster_url", url);
        return movie;
    }

    private void initFirebase() {

        mUserName = mFirebaseUser.getDisplayName();
        mUserEmail = mFirebaseUser.getEmail();
        // store app title to 'app_title' node

        mFirebaseInstance = FirebaseDatabase.getInstance();
        mFirebaseInstance.getReference(Constant.APP_NODE_FIREBASE).setValue(Constant
                .APP_TITLE_FIREBASE);

        // get reference to 'users' node
        mUserCloudEndPoint = mFirebaseInstance.getReference(Constant.USERS_NODE_FIREBASE);
    }

    private boolean checkUser(DataSnapshot dataSnapshot) {
        userId = mFirebaseInstance.getReference().getKey();
        for (DataSnapshot categorySnapshot : dataSnapshot.getChildren()) {
            mUser = categorySnapshot.getValue(MovieUser.class);
            if (mUser.getEmail().equals(mUserEmail)) {
                userId = categorySnapshot.getKey();
                return true;
            }
        }
        return false;
    }

    private void showButtonFavourite() {
        //show button Favourite
        mFirebaseAuth = FirebaseAuth.getInstance();
        mFirebaseUser = mFirebaseAuth.getCurrentUser();

        if (mFirebaseUser == null) {
            mImageFavorite.setVisibility(View.INVISIBLE);
        } else {

            initFirebase();
            mUserCloudEndPoint.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {

                    if (checkUser(dataSnapshot)) {
                        HashMap<String, String> movie = new HashMap<String, String>();
                        movie = getMovie(movieId.toString(), titleMovie, urlPoster);

                        Integer key = 0;
                        for (HashMap<String, String> favouriteMovie : mUser.getFavouriteMovies()) {

                            if (favouriteMovie.equals(movie)) {
                                mImageFavorite.setSelected(true);
                                break;
                            }
                            key++;
                        }
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
            mImageFavorite.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.relative_play:
                mRelativeLayout.setVisibility(View.INVISIBLE);
                mRelativeLayout.setVisibility(View.GONE);
                mYouTubePlayer.initialize(Constant.GOOGLE_API_KEY, this);
                break;
            case R.id.imageFavorite:
                initFirebase();
                if (!mImageFavorite.isSelected()) {
                    // Check for already existed user
                    mUserCloudEndPoint.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            Boolean isExistUser = false;
                            if (checkUser(dataSnapshot)) {
                                    int numOfMovie = mUser.getFavouriteMovies().size();
                                    addMovie(movieId, titleMovie,
                                            urlPoster, numOfMovie);
                            } else {
                                createUser(mUserName, mUserEmail, movieId, titleMovie, urlPoster);
                                mUserCloudEndPoint.getDatabase();
                            }
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });
                    mImageFavorite.setSelected(true);
                } else {

                    mUserCloudEndPoint.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            if (checkUser(dataSnapshot)) {
                                HashMap<String, String> movie = new HashMap<String, String>();
                                movie = getMovie(movieId.toString(), titleMovie, urlPoster);

                                ArrayList<HashMap<String, String>> favouriteList = new ArrayList
                                        <HashMap<String, String>>();
                                for (HashMap<String, String> favouriteMovie : mUser
                                        .getFavouriteMovies()) {
                                    favouriteList.add(favouriteMovie);
                                }

                                for (HashMap<String, String> movie1 : favouriteList) {
                                    if (movie1.equals(movie)) {
                                        favouriteList.remove(movie1);
                                        break;
                                    }
                                }

                                mUserCloudEndPoint.child(userId).child("favouriteMovies")
                                        .setValue(favouriteList);
                            }
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });

                    mImageFavorite.setSelected(false);
                }

                break;
            default:
                break;
        }
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Log.d(TAG, R.string.connection_failed + connectionResult.getErrorMessage());
    }
}
