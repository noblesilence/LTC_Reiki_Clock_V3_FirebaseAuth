package com.learnteachcenter.ltcreikiclock.data;

/**
 * Created by aye2m on 10/17/17.
 */

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.ForeignKey;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.Index;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

import java.util.UUID;

/**
 * Created by aye2m on 3/6/16.
 *
 * Each Position holds:
 *      - ID
 *      - Title
 *      - Duration in mm:ss
 *
 *  E.g.
 *      - 1
 *      - Crown Chakra
 *      - 3:00
 *
 */

@Entity(foreignKeys = @ForeignKey(
        entity = Reiki.class,
        parentColumns = "id",
        childColumns = "reikiId",
        onDelete = ForeignKey.CASCADE))
public class Position {
    int seqNo;  // To remember the item's position on the list. Will change when user reorders the list.
    String reikiId; // Foreign Key

    @NonNull
    @PrimaryKey String positionId; // Primary Key
    String title;
    String duration;
    @Ignore long durationSeconds;

    public Position(String reikiId, int seqNo, String title, String duration) {
        this.reikiId = reikiId;
        this.positionId = UUID.randomUUID().toString();
        this.seqNo = seqNo;
        this.title = title;
        this.duration = duration;
    }

    @Ignore
    public Position(String reikiId, String positionId, int seqNo, String title, String duration){
        this.reikiId = reikiId;
        this.positionId = positionId;
        this.seqNo = seqNo;
        this.title = title;
        this.duration = duration;
    }

    public String getReikiId() {
        return reikiId;
    }

    public String getId() { return positionId; }
    public void setId(String positionId) { this.positionId = positionId; }

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

    public String getDuration() {
        return duration;
    }

    public long getDurationSeconds() {

        String[] durationComponents = duration.split(":");

        if(durationComponents.length == 3) {
            durationSeconds = Long.parseLong(durationComponents[0]) * 3600
                    + Long.parseLong(durationComponents[1]) * 60
                    + Long.parseLong(durationComponents[2]);
        }
        else if(durationComponents.length == 2) {
            durationSeconds = Long.parseLong(durationComponents[0]) * 60
                    + Long.parseLong(durationComponents[1]);
        }
        else {
            durationSeconds = Long.parseLong(durationComponents[0]);
        }

        return durationSeconds;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }
}