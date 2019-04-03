package com.learnteachcenter.ltcreikiclock.data;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;
import androidx.annotation.NonNull;

import com.squareup.moshi.Json;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;


@Entity(
        tableName = "Reiki"
)
public class Reiki implements Serializable {
    @Json(name = "_id")
    @NonNull
    @PrimaryKey
    @ColumnInfo(name = "id", index = true)
    private String id;

    @Json(name="seqNo")
    @ColumnInfo(name="seqNo")
    private int seqNo;

    @Json(name="title")
    @ColumnInfo(name = "title")
    private String title;

    @Json(name="description")
    @ColumnInfo(name="description")
    private String description;

    @Json(name="playMusic")
    @ColumnInfo(name="playMusic")
    private Boolean playMusic;

    @Json(name="positions")
    @Ignore
    private List<Position> positions;

    public Reiki(){}

    public Reiki(String id, int seqNo, String title, String description, Boolean playMusic) {
        this.id = id;
        this.seqNo = seqNo;
        this.title = title;
        this.description = description;
        this.playMusic = playMusic;
    }

    public Reiki(String id, int seqNo, String title, String description, Boolean playMusic, ArrayList<Position> positions) {
        this.id = id;
        this.seqNo = seqNo;
        this.title = title;
        this.description = description;
        this.playMusic = playMusic;
        this.positions = positions;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public int getSeqNo() {
        return seqNo;
    }

    public void setSeqNo(int seqNo) {
        this.seqNo = seqNo;
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

    public Boolean getPlayMusic() {
        return playMusic;
    }

    public void setPlayMusic(Boolean playMusic) {
        this.playMusic = playMusic;
    }

    public List<Position> getPositions() {
        return positions;
    }

    public void setPositions(ArrayList<Position> positions) {
        this.positions = positions;
    }
}
