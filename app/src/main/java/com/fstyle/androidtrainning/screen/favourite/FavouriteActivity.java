package com.fstyle.androidtrainning.screen.favourite;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.fstyle.androidtrainning.R;

/**
 * Created by ossierra on 12/27/17.
 */

public class FavouriteActivity extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater,
            ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.activity_favourite, container, false);
    }
}
