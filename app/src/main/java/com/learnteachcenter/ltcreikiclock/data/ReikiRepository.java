package com.learnteachcenter.ltcreikiclock.data;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.db.SupportSQLiteDatabase;
import android.support.annotation.WorkerThread;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import javax.inject.Inject;

/**
 * Created by aye2m on 10/14/17.
 * <p>
 * Credit:
 * https://www.youtube.com/playlist?list=PLEVlop6sMHCpClb5MoCSChoufFvax9lDO
 */

public class ReikiRepository {

    private final ReikiDao reikiDao;

    @Inject
    public ReikiRepository(ReikiDao reikiDao) {
        this.reikiDao = reikiDao;
    }

    // Reikis

    public LiveData<List<Reiki>> getListOfReikis() {
        //Even though it's LiveData, still the Observer Pattern.
        return reikiDao.getReikis();
    }

    public void populateReikiDatabase(){
        Reiki defaultReiki = new Reiki(
                "Chakra Balancing",
                "Balancing 7 different energy levels",
                true,
                1
                );

        reikiDao.insertReiki(defaultReiki);

        // Insert 7 positions
        String titles[] = {"Crown", "Third Eye", "Throat", "Heart", "Solar Plexus", "Sacral", "Root"};

        for (int i = 0; i < titles.length; i++) {
            Position position = new Position(
                    defaultReiki.getId(),
                    i+1,
                    titles[i],
                    "3:15"
            );

            reikiDao.insertPosition(position);
        }
    }

    public LiveData<Reiki> getReiki(String id) {
        return reikiDao.getReikiById(id);
    }

    public Long createNewReiki(Reiki reiki) {
        return reikiDao.insertReiki(reiki);
    }

    public void updateReiki(Reiki reiki) { reikiDao.updateReiki(reiki); }

    public void updateReikis(Reiki... reikis) {
        reikiDao.updateReiki(reikis);
    }

    public void updatePositions(Position... positions) {
        reikiDao.updatePosition(positions);
    }

    /*
        In order to assure integrity of seqNos, when a given Item is deleted; all seqNos which are
        greater in value must be decremented by 1.
    */
    public void deleteReiki(Reiki reiki) {
        reikiDao.deleteReiki(reiki);

        int seqNo = reiki.getSeqNo();

        //Get all Reikis with seqNo greater than deleted Reiki
        List<Reiki> reikiList = reikiDao.getReikisGreaterThanSeqNo(seqNo);

        for (Reiki tempReiki: reikiList){
            //decrement by 1
            tempReiki.setSeqNo(
                    tempReiki.getSeqNo() - 1
            );

            //may be easier/faster to do bulk update but I'm not certain
            // TODO: move this to outside for loop and pass array
            reikiDao.updateReiki(tempReiki);
        }
    }

    // Positions
    public LiveData<List<Position>> getListOfPositionsByReikiId(String reikiId) {
        return reikiDao.getPositionsByReikiId(reikiId);
    }

    public LiveData<Position> getPosition(String reikiId, int seqNo) {
        return reikiDao.getPositionById(reikiId, seqNo);
    }

    public Long createNewPosition(Position position) {
        return reikiDao.insertPosition(position);
    }
    public void updatePosition(Position position) { reikiDao.updatePosition(position); }

    public void deletePosition(Position position) {
        reikiDao.deletePosition(position);

        //Get all positions with seqNo greater than deleted position
        List<Position> positionList = reikiDao.getPositionsGreaterThanSeqNo(
                position.getReikiId(),
                position.getSeqNo()
                );

        // Update their seqNo
        for (Position tempPosition: positionList){

            //decrement by 1
            tempPosition.setSeqNo(
                    tempPosition.getSeqNo() - 1
            );

            // TODO: move this to outside for loop (pass array)
            reikiDao.updatePosition(tempPosition);
        }

    }

}
