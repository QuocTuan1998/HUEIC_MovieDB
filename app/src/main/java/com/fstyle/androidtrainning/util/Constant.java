package com.fstyle.androidtrainning.util;

/**
 * Created by ossierra on 12/25/17.
 */

public final class Constant {

    private Constant() {
        // no-op
    }

    public static final String API_KEY = "c2c3be48986f7322bf0ca19e48af1820";
    public static final String API_URL_POPULAR_MOVIE = "http://api.themoviedb.org/3/discover/movie?"
            + "sort_by=popularity.desc?&api_key=" + API_KEY;

}
