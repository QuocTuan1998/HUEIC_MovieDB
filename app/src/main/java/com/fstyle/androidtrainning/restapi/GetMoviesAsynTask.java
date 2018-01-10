package com.fstyle.androidtrainning.restapi;

import android.os.AsyncTask;
import com.fstyle.androidtrainning.listener.CallAPIListener;
import com.fstyle.androidtrainning.model.MovieListResponse;
import com.fstyle.androidtrainning.util.Constant;
import com.fstyle.androidtrainning.model.Movie;
import com.fstyle.androidtrainning.util.StringUtils;
import com.google.gson.Gson;
import java.io.IOException;
import java.util.List;
import org.json.JSONException;

/**
 * Created by ossierra on 12/25/17.
 */

public class GetMoviesAsynTask extends AsyncTask<String, Void, List<Movie>> {
    private CallAPIListener mCallAPIListener;

    public GetMoviesAsynTask(CallAPIListener mCallAPIListener) {
        this.mCallAPIListener = mCallAPIListener;
    }

    @Override
    protected List<Movie> doInBackground(String... strings) {
        try {
            String json;
            if (strings.length == 0) {
                json = StringUtils.getJSONStringFromURL(Constant.API_URL_POPULAR_MOVIE);
            } else {
                json = StringUtils.getJSONStringFromURL(strings[0]);
            }
            MovieListResponse mMovieList = new Gson().fromJson(json, MovieListResponse.class);
            return mMovieList.getMovies();
        } catch (IOException e) {
            mCallAPIListener.onCallAPIError(e);
        } catch (JSONException e) {
            mCallAPIListener.onCallAPIError(e);
        }
        return null;
    }

    @Override
    protected void onPostExecute(List<Movie> movies) {
        super.onPostExecute(movies);
        mCallAPIListener.onCallAPISuccess(movies);
    }
}
