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
    public static final String EXTRA_MOVIE_ID = "EXTRA_MOVIE_ID";
    public static final String MAX_POINT = "/10";
    public static final String MINUTE = " min";
    public static final String DATE_FORMAT_DD_MMM_YYYY = "dd MMM yyyy";
    public static final String DATE_FORMAT_DD_MM_YYYY = "yyyy-MM-dd";
    public static final String LANGUAGE = "en-US";
    public static final int DEFAULT_PAGE = 1;
    public static final String TITLE_TOOLBAR = "Search";
    public static final String GOOGLE_API_KEY = "AIzaSyA99nefcNFvOztPiH4lp8YtcSjz072UsDU";
    public static final String OFFICIAL = "Official";
    public static final int FIRST_TRAILER = 0;
}
