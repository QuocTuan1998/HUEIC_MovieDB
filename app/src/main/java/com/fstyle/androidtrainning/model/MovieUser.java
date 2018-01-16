package com.fstyle.androidtrainning.model;

import java.util.HashMap;
import java.util.List;

/**
 * Created by ossierra on 1/4/18.
 */

public class MovieUser {

    private String name;
    private String email;
    private List<HashMap<String, String>> favouriteMovies;

    public MovieUser() {
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public List<HashMap<String, String>> getFavouriteMovies() {
        return favouriteMovies;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setFavouriteMovies(List<HashMap<String, String>> favouriteMovies) {
        this.favouriteMovies = favouriteMovies;
    }
}