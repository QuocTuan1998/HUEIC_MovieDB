package com.fstyle.androidtrainning.screen.detailmovie;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;
import com.fstyle.androidtrainning.R;
import com.fstyle.androidtrainning.model.Movie;
import com.fstyle.androidtrainning.util.Constant;
import butterknife.BindView;
import butterknife.ButterKnife;


/**
 * Created by ossierra on 1/2/18.
 */

public class DetailsMovieActivity extends AppCompatActivity {

    @BindView(R.id.text_movie_title)
    TextView mTextTitle;

    public DetailsMovieActivity() {
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_details);
        Movie movie = getIntent().getParcelableExtra(Constant.EXTRA_MOVIE_ID);
        ButterKnife.bind(this);
        mTextTitle.setText(movie.getOriginalTitle());
    }
}
