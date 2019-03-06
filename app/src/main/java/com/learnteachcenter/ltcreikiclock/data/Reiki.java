package com.learnteachcenter.ltcreikiclock.data;

/*
 * Created by aye2m on 10/11/17.
 * Created a new project to get com.learnteachcenter.ltcreikiclock (test) package
 *
 * Created by aye2m on 3/4/16.
 *
 * Reiki and Position have one-to-many relationship.
 * Each Reiki has many Positions.
 *
 * How to define one-to-many relationship in Room:
 * https://www.bignerdranch.com/blog/room-data-storage-for-everyone/
 *
 * -----------------------------
 * Reiki Table and its columns
 * -----------------------------
 * A Reiki holds
 *      - Title
 *      - Description
 *      - Play music or not
 *      - A list of Positions (Title, Duration in mm:ss)
 *
 *  E.g.
 *      - Chakra Balancing
 *      - Balancing 7 different energy levels
 *      - True
 *      - A list of Positions
 *          (Crown, 3:00)
 *          (Third Eye, 3:00)
 *          (Throat, 3:00)
 *          (Heart, 3:00)
 *          (Solar Plexus, 3:00)
 *          (Sacral, 3:00)
 *          (Root, 3:00)
 *
 */

import java.util.UUID;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

@Entity
public class Reiki {

    @NonNull
    @PrimaryKey
    private String id;
    private int seqNo; // To remember the item's position on the list. Will change when user reorders the list.
    private String title;
    private String description;
    private boolean playMusic;

    public Reiki(String id, String title, String description, boolean playMusic) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.playMusic = playMusic;
    }

    @Ignore
    public Reiki(String title, String description, boolean playMusic, int seqNo) {
        id = UUID.randomUUID().toString();
        this.title = title;
        this.description = description;
        this.playMusic = playMusic;
        this.seqNo = seqNo;
    }

    @Ignore
    public Reiki(String id, String title, String description, boolean playMusic, int seqNo) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.playMusic = playMusic;
        this.seqNo = seqNo;
    }

    public int getSeqNo() { return seqNo; }

    public void setSeqNo(int seqNo) { this.seqNo = seqNo; }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public boolean getPlayMusic() { return playMusic; }

    public void setPlayMusic(boolean playMusic) { this.playMusic = playMusic; }
}

