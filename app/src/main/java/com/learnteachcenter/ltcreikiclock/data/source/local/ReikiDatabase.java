package com.learnteachcenter.ltcreikiclock.data.source.local;

import androidx.room.Database;
import androidx.room.RoomDatabase;

import com.learnteachcenter.ltcreikiclock.data.Position;
import com.learnteachcenter.ltcreikiclock.data.Reiki;

/**
 * Created by aye2m on 10/13/17.
 */
@Database(entities = {Reiki.class, Position.class}, version = 1)
public abstract class ReikiDatabase extends RoomDatabase {
    public abstract ReikiDao reikiDao();
}
