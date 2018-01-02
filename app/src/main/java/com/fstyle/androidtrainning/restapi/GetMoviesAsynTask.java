package com.fstyle.androidtrainning.restapi;

import android.os.AsyncTask;
import com.fstyle.androidtrainning.listener.CallAPIListener;
import com.fstyle.androidtrainning.model.MovieListResponse;
import com.fstyle.androidtrainning.util.Constant;
import com.fstyle.androidtrainning.model.Movie;
import com.google.gson.Gson;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.Charset;
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
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected List<Movie> doInBackground(String... strings) {
        try {
            String json = getJSONStringFromURL(Constant.API_URL_POPULAR_MOVIE);
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

    private String getJSONStringFromURL(String urlString) throws IOException, JSONException {

        HttpURLConnection urlConnection = null;

        URL url = new URL(urlString);

        urlConnection = (HttpURLConnection) url.openConnection();

        urlConnection.setRequestMethod("GET");
        urlConnection.setReadTimeout(10000 /* milliseconds */);
        urlConnection.setConnectTimeout(15000 /* milliseconds */);

        urlConnection.setDoOutput(true);

        urlConnection.connect();

        BufferedReader br = new BufferedReader(new InputStreamReader(url.openStream(), Charset.defaultCharset()));

        StringBuilder sb = new StringBuilder();
        String line;
        while ((line = br.readLine()) != null) {
            line = line + "\n";
            sb.append(line);
        }
        br.close();

        String jsonString = sb.toString();

        urlConnection.disconnect();
        return jsonString;
    }
}
