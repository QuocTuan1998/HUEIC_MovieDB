package com.fstyle.androidtrainning.screen.detailmovie;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
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
import com.fstyle.androidtrainning.model.Trailer;
import com.fstyle.androidtrainning.restapi.GetMovieAsynTask;
import com.fstyle.androidtrainning.restapi.GetTrailerMovieAsynTank;
import com.fstyle.androidtrainning.util.Constant;
import com.fstyle.androidtrainning.util.DateTimeUtils;
import com.fstyle.androidtrainning.util.StringUtils;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerFragment;
import com.google.android.youtube.player.YouTubeThumbnailLoader;
import com.google.android.youtube.player.YouTubeThumbnailView;
import java.util.Date;
import java.util.List;

/**
 * Created by ossierra on 1/2/18.
 */

public class DetailsMovieActivity extends AppCompatActivity
        implements CallAPIListener, YouTubePlayer.OnInitializedListener,
        YouTubeThumbnailView.OnInitializedListener, View.OnClickListener{
    private ImageView mImageFavorite, mImageBigView, mImageSmallView;
    private TextView mTextTitleMovie, mTextPublishTime, mTextTimeMovie;
    private TextView mTextKindMovie, mTextRate, mTextOverview;
    private YouTubeThumbnailView mThumbnailView;
    private YouTubePlayerFragment mYouTubePlayer;
    private String mYoutubeKey;
    private YouTubePlayer.OnInitializedListener mOnPlayerInitListener;
    private RelativeLayout mRelativeLayout;
    private static final String TAG = "DetailsMovieActivity";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_details);

        Integer movieId = getIntent().getIntExtra(Constant.EXTRA_MOVIE_ID, 0);

        initViews();

        new GetMovieAsynTask(DetailsMovieActivity.this).execute(
                "https://api.themoviedb.org/3/movie/" + movieId + "?api_key="
                        + Constant.API_KEY + "&language=" + Constant.LANGUAGE
        );
        new GetTrailerMovieAsynTank(DetailsMovieActivity.this).execute(
                "https://api.themoviedb.org/3/movie/" + movieId
                        + "/videos?api_key=" + Constant.API_KEY
        );

        mImageFavorite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!mImageFavorite.isSelected()) {
                    mImageFavorite.setSelected(true);
                } else {
                    mImageFavorite.setSelected(false);
                }
            }
        });
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
                String urlPoster = StringUtils.convertPosterPathToUrlPoster(movie.getPosterPath());
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
        if (mList != null) {
            mThumbnailView.setVisibility(View.VISIBLE);
            if (mList instanceof List) {
                if (((List<Movie>) mList).get(0) instanceof Movie) {
                    List<Movie> movies = mList;
                    for (Movie movie : movies) {
                        fillData(movie);
                    }
                } else {
                    List<Trailer> trailers = mList;
                    showMovieOnGrid(trailers);
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

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.relative_play:
                mRelativeLayout.setVisibility(View.INVISIBLE);
                mRelativeLayout.setVisibility(View.GONE);
                mYouTubePlayer.initialize(Constant.GOOGLE_API_KEY, this);
                break;
            default:
                break;
        }
    }
}
