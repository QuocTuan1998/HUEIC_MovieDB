package com.fstyle.androidtrainning.listener;

import java.util.List;
/**
 * Created by ossierra on 12/25/17.
 */

public interface CallAPIListener<T> {

    void onStartCallAPI();

    void onCallAPISuccess(List<T> data);

    void onCallAPIError(Exception e);
}
