package com.fstyle.androidtrainning.screen.detailmovie;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import com.bumptech.glide.Glide;
import com.fstyle.androidtrainning.R;
import com.fstyle.androidtrainning.model.Movie;
import com.fstyle.androidtrainning.util.Constant;
import com.fstyle.androidtrainning.util.DateTimeUtils;
import com.fstyle.androidtrainning.util.StringUtils;
import java.util.Date;

/**
 * Created by ossierra on 1/2/18.
 */

public class DetailsMovieActivity extends AppCompatActivity {
    private ImageView mImageFavorite, mImageBigView, mImageSmallView;
    private TextView mTextTitleMovie, mTextPublishTime, mTextTimeMovie;
    private TextView mTextKindMovie, mTextRate, mTextOverview;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_details);

        Movie movie = getIntent().getParcelableExtra(Constant.EXTRA_MOVIE_ID);

        initViews();
        fillData(movie);

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
    }
    private void fillData(Movie movie) {
        String urlBackdrop = StringUtils.convertPosterPathToUrlPoster(movie.getBackdropPath());
        String urlPoster = StringUtils.convertPosterPathToUrlPoster(movie.getPosterPath());
        String genresCommaSeparated = StringUtils
                .convertListToStringCommaSeparated(movie.getMovieGenres());
        String rateMovie = movie.getVoteAverage() + Constant.MAX_POINT;
        String runTimeMovie = movie.getRuntime() + Constant.MINUTE;
        Date date =
                DateTimeUtils.convertStringToDate(movie.getReleaseDate(),
                        Constant.DATE_FORMAT_DD_MM_YYYY);
        String releaseDate = DateTimeUtils.getStrDateTimeFormatted(date,
                Constant.DATE_FORMAT_DD_MMM_YYYY);

        Glide.with(this).load(urlBackdrop).into(mImageBigView);
        Glide.with(this).load(urlPoster).into(mImageSmallView);
        mTextTitleMovie.setText(movie.getTitle());
        mTextPublishTime.setText(releaseDate);
        mTextTimeMovie.setText(runTimeMovie);
        mTextKindMovie.setText(genresCommaSeparated);
        mTextRate.setText(rateMovie);
        mTextOverview.setText(movie.getOverview());
    }
}
