package com.fstyle.androidtrainning.restapi;

import android.os.AsyncTask;
import com.fstyle.androidtrainning.listener.CallAPIListener;
import com.fstyle.androidtrainning.model.Movie;
import com.fstyle.androidtrainning.util.StringUtils;
import com.google.gson.Gson;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import org.json.JSONException;

/**
 * Created by MyPC on 09/01/2018.
 */

public class GetMovieAsynTask  extends AsyncTask<String, Void, Movie> {
    private static final String TAG = "GetMovieAsynTask";
    
    private CallAPIListener mCallAPIListener;

    public GetMovieAsynTask(CallAPIListener mCallAPIListener) {
        this.mCallAPIListener = mCallAPIListener;
    }

    @Override
    protected Movie doInBackground(String... strings) {
        String json;
        try {
            json = StringUtils.getJSONStringFromURL(strings[0]);
            Movie mMovie = new Gson().fromJson(json, Movie.class);
            return mMovie;
        } catch (IOException e) {
            mCallAPIListener.onCallAPIError(e);
        } catch (JSONException e) {
            mCallAPIListener.onCallAPIError(e);
        }
        return null;
    }

    @Override
    protected void onPostExecute(Movie movie) {
        super.onPostExecute(movie);
        List<Movie> movieList = new ArrayList<>();
        movieList.add(movie);
        mCallAPIListener.onCallAPISuccess(movieList);
    }
}
