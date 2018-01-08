package com.fstyle.androidtrainning.restapi;

import android.os.AsyncTask;
import com.fstyle.androidtrainning.listener.CallAPIListener;
import com.fstyle.androidtrainning.model.Trailer;
import com.fstyle.androidtrainning.model.TrailerListResponse;
import com.fstyle.androidtrainning.util.Constant;
import com.fstyle.androidtrainning.util.StringUtils;
import com.google.gson.Gson;
import java.io.IOException;
import java.util.List;
import org.json.JSONException;

/**
 * Created by MyPC on 08/01/2018.
 */

public class GetTrailerMovieAsynTank extends AsyncTask<String, Void, List<Trailer>> {

    private final CallAPIListener mCallAPIListener;

    public GetTrailerMovieAsynTank(CallAPIListener mCallAPIListener) {
        this.mCallAPIListener = mCallAPIListener;
    }

    @Override
    protected List<Trailer> doInBackground(String... strings) {
        try {
            String json;
            if (strings.length == 0) {
                json = StringUtils.getJSONStringFromURL(Constant.API_URL_POPULAR_MOVIE);
            } else {
                json = StringUtils.getJSONStringFromURL(strings[0]);
            }
            TrailerListResponse mTrailerList = new Gson().fromJson(json, TrailerListResponse.class);
            return mTrailerList.getTrailers();
        } catch (IOException e) {
            mCallAPIListener.onCallAPIError(e);
        } catch (JSONException e) {
            mCallAPIListener.onCallAPIError(e);
        }
        return null;
    }
    @Override
    protected void onPostExecute(List<Trailer> trailers) {
        super.onPostExecute(trailers);
        mCallAPIListener.onCallAPISuccess(trailers);
    }
}

