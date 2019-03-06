package com.learnteachcenter.ltcreikiclock.data;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class ReikiResponse {
    @SerializedName("_id")
    private String _id;

    @SerializedName("user")
    private String user;

    @SerializedName("seqNo")
    private String seqNo;

    @SerializedName("title")
    private String title;

    @SerializedName("description")
    private String description;

    @SerializedName("positions")
    private List<Position> positions = new ArrayList<>();

    @SerializedName("playMusic")
    private boolean playMusic;

    @SerializedName("__v")
    private int __v;

    public String getId() {
        return _id;
    }

    public void setId(String _id) {
        this._id = _id;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getSeqNo() {
        return seqNo;
    }

    public void setSeqNo(String seqNo) {
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

    public List<Position> getPositions() {
        return positions;
    }

    public void setPositions(List<Position> positions) {
        this.positions = positions;
    }

    public boolean isPlayMusic() {
        return playMusic;
    }

    public void setPlayMusic(boolean playMusic) {
        this.playMusic = playMusic;
    }

    public int get__v() {
        return __v;
    }

    public void set__v(int __v) {
        this.__v = __v;
    }
}
