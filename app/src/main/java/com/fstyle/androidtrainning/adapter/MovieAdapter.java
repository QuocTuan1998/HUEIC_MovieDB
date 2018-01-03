package com.fstyle.androidtrainning.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import com.bumptech.glide.Glide;
import com.fstyle.androidtrainning.listener.OnRecyclerViewItemListener;
import com.fstyle.androidtrainning.model.Movie;
import com.fstyle.androidtrainning.R;
import java.util.List;
import butterknife.BindView;
import butterknife.ButterKnife;

public class MovieAdapter extends RecyclerView.Adapter<MovieAdapter.MovieHolder> {

    private List<Movie> movies;
    private int rowLayout;
    private OnRecyclerViewItemListener mOnRecyclerViewItemListener;

    public MovieAdapter(List<Movie> movies, int rowLayout) {
        this.movies = movies;
        this.rowLayout = rowLayout;
    }

    @Override
    public MovieHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(rowLayout, parent, false);
        return new MovieHolder(view, mOnRecyclerViewItemListener);
    }

    @Override
    public void onBindViewHolder(MovieHolder holder, final int position) {
        holder.bind(movies.get(position));
    }

    @Override
    public int getItemCount() {
        return movies.size();
    }

    public void setOnRecyclerViewItemListener(
            OnRecyclerViewItemListener onRecyclerViewItemListener) {
        mOnRecyclerViewItemListener = onRecyclerViewItemListener;
    }

    public static class MovieHolder extends RecyclerView.ViewHolder{

        @BindView(R.id.movie_title)
        TextView movieTitle;

        @BindView(R.id.movie_poster)
        ImageView moviePoster;

        private OnRecyclerViewItemListener mOnRecyclerViewItemListener;
        private Movie mMovie;

        public MovieHolder(View itemView, OnRecyclerViewItemListener onRecyclerViewItemListener) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            mOnRecyclerViewItemListener = onRecyclerViewItemListener;
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mOnRecyclerViewItemListener.onItemClick(mMovie);
                }
            });
        }

        public void bind(Movie movie) {
            mMovie = movie;
            movieTitle.setText(movie.getOriginalTitle());
            Glide.with(itemView.getContext()).load(movie.getPosterUrl()).into(moviePoster);
        }
    }
}
