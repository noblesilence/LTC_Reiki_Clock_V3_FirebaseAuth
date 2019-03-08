package com.learnteachcenter.ltcreikiclock.utils;

import android.support.v7.util.DiffUtil;

import com.learnteachcenter.ltcreikiclock.data.Reiki;

import java.util.List;

/**
 * Created by R_KAY on 11/29/2017.
 */

public class CustomDiffUtilCallback extends DiffUtil.Callback {

    List<Reiki> oldList;
    List<Reiki> newList;

    @Override
    public int getOldListSize() {
        return oldList.size();
    }

    @Override
    public int getNewListSize() {
        return newList.size();
    }

    @Override
    public boolean areItemsTheSame(int oldItemPosition, int newItemPosition) {
        return oldList.get(oldItemPosition) == newList.get(newItemPosition);
    }

    @Override
    public boolean areContentsTheSame(int oldItemPosition, int newItemPosition) {
        return false;
    }
}
