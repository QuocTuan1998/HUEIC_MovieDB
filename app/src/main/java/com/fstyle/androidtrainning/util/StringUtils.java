package com.fstyle.androidtrainning.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.List;
import org.json.JSONException;

/**
 * Created by MyPC on 02/01/2018.
 */

public final class StringUtils {
    private static final String TAG = "StringUtils";
    private static final String URL_POSTER_API = "https://image.tmdb.org/t/p/w500/";
    private static final String MORE = " ...";
    private static final String OPEN_PARENTHESE = " (";
    private static final String CLOSE_PARENTHESE = ")";
    private static final String SEPARATOR = ", ";
    private static final int BEGIN_INDEX = 0;
    private static final int END_INDEX = 28;
    private static final int YEAR = 4;

    private StringUtils() {
        //No-op
    }

    public static String convertPosterPathToUrlPoster(String posterPath) {
        return URL_POSTER_API + posterPath;
    }

    public static String convertLongTitleToShortTitle(String titleMovie, String releaseDate) {
        if (releaseDate.equals("")) {
            return titleMovie;
        } else {
            String year = releaseDate.substring(BEGIN_INDEX, YEAR);
            if (titleMovie.length() > END_INDEX) {
                titleMovie = titleMovie.substring(BEGIN_INDEX, END_INDEX)
                        + MORE
                        + OPEN_PARENTHESE
                        + year
                        + CLOSE_PARENTHESE;
                return titleMovie;
            } else {
                titleMovie = titleMovie + OPEN_PARENTHESE + year + CLOSE_PARENTHESE;
                return titleMovie;
            }
        }
    }

    public static String convertLongNameToShortName(String nameCast) {
        if (nameCast.length() > END_INDEX) {
            nameCast = nameCast.substring(BEGIN_INDEX, END_INDEX) + MORE;
            return nameCast;
        } else {
            return nameCast;
        }
    }

    public static String convertListToStringCommaSeparated(List<String> strings) {
        if (strings.size() == 0) {
            String result = "Unknown genre";
            return result;
        }
        StringBuilder builder = new StringBuilder();
        for (String s : strings) {
            builder.append(s);
            builder.append(SEPARATOR);
        }
        String result = builder.toString();
        result = result.substring(BEGIN_INDEX, result.length() - SEPARATOR.length());
        return result;
    }

    public static String getJSONStringFromURL(String urlString) throws IOException, JSONException {

        HttpURLConnection urlConnection = null;

        URL url = new URL(urlString);

        urlConnection = (HttpURLConnection) url.openConnection();

        urlConnection.setRequestMethod("GET");
        urlConnection.setReadTimeout(10000 /* milliseconds */);
        urlConnection.setConnectTimeout(15000 /* milliseconds */);

        urlConnection.setDoOutput(true);

        urlConnection.connect();

        BufferedReader br = new BufferedReader(new InputStreamReader(url.openStream(),
                Charset.defaultCharset()));

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

