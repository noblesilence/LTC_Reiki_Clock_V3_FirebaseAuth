package com.learnteachcenter.ltcreikiclock.data;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.RoomDatabase;

/**
 * Created by aye2m on 10/13/17.
 */
@Database(entities = {Reiki.class, Position.class}, version = 3)
public abstract class ReikiDatabase extends RoomDatabase {
    public abstract ReikiDao reikiDao();
}
