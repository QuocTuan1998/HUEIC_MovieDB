package com.fstyle.androidtrainning.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import com.bumptech.glide.Glide;
import com.fstyle.androidtrainning.model.Movie;
import com.fstyle.androidtrainning.R;
import java.util.List;

public class MovieAdapter extends RecyclerView.Adapter<MovieAdapter.MusicHolder> {

    private List<Movie> arrMusic;
    private int rowLayout;
    private View view;

    public MovieAdapter(List<Movie> arrMusic, int rowLayout) {
        this.arrMusic = arrMusic;
        this.rowLayout = rowLayout;
    }

    @Override
    public MovieAdapter.MusicHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        view = LayoutInflater.from(parent.getContext()).inflate(rowLayout, parent, false);
        return new MusicHolder(view);
    }

    @Override
    public void onBindViewHolder(MusicHolder holder, final int position) {
        Movie movie = arrMusic.get(position);
        holder.tvMovieTitle.setText(movie.getOriginalTitle());
        Glide.with(view).load(movie.getPosterUrl()).into(holder.tvMoviePoster);

    }

    @Override
    public int getItemCount() {
        return arrMusic.size();
    }

    public static class MusicHolder extends RecyclerView.ViewHolder {
        TextView tvMovieTitle;
        ImageView tvMoviePoster;

        public MusicHolder(View v) {
            super(v);
            tvMovieTitle = (TextView) v.findViewById(R.id.movie_title);
            tvMoviePoster = (ImageView) v.findViewById(R.id.movie_poster);
        }

    }

}
