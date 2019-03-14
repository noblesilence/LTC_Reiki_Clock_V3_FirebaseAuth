package com.learnteachcenter.ltcreikiclock.data.source;

import android.arch.lifecycle.LiveData;
import android.util.Log;

import com.learnteachcenter.ltcreikiclock.BuildConfig;
import com.learnteachcenter.ltcreikiclock.Utils;
import com.learnteachcenter.ltcreikiclock.data.Position;
import com.learnteachcenter.ltcreikiclock.data.Reiki;
import com.learnteachcenter.ltcreikiclock.data.source.local.ReikiDao;
import com.learnteachcenter.ltcreikiclock.data.source.remote.ApiInterface;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.Observable;

/**
 * Created by aye2m on 10/14/17.
 * <p>
 * Credit:
 * https://www.youtube.com/playlist?list=PLEVlop6sMHCpClb5MoCSChoufFvax9lDO
 */

public class ReikiRepository {
    private static final String TAG = "Reiki";

    private final ApiInterface apiInterface;
    private final ReikiDao reikiDao;
    private final Utils utils;

    @Inject
    public ReikiRepository(
            ApiInterface apiInterface,
            ReikiDao reikiDao,
            Utils utils) {
        this.apiInterface = apiInterface;
        this.reikiDao = reikiDao;
        this.utils = utils;
    }

    // Reikis

    public Observable<List<Reiki>> getListOfReikis() {
        Boolean hasConnection = utils.isConnectedToInternet();
        Observable<List<Reiki>> observableFromApi = null;

        if(hasConnection) {
            observableFromApi = getReikisFromApi();
        }

        Observable<List<Reiki>> observableFromDb = getReikisFromDb();

        if(hasConnection) {
            return observableFromApi;
        } else {
            return observableFromDb;
        }
    }

    private Observable<List<Reiki>> getReikisFromApi(){
        Log.d(TAG, "getReikisFromApi: " + BuildConfig.URL);
        return apiInterface.getSampleReikis()
                .doOnNext(reikis -> {
                    Log.e(TAG, "getReikisFromApi: " + reikis);
                    for(Reiki reiki:reikis) {
                        reikiDao.insertReiki(reiki);

                        Log.d(TAG, "position 0 title: " + reiki.getPositions().get(0).getTitle());

                        // TODO: insert each position as well
                    }
                });
    }

    private Observable<List<Reiki>> getReikisFromDb(){
        Log.d(TAG, "getReikisFromDb: ");
        return reikiDao.getReikis()
                .toObservable()
                .doOnNext(reikis -> {
                    Log.e(TAG, "getReikisFromDb: " + reikis.size() );
                });
    }

//    public void populateReikiDatabase(){
//        Reiki defaultReiki = new Reiki(
//                1,
//                "Chakra Balancing",
//                "Balancing 7 different energy levels",
//                true,
//                1
//                );
//
//        reikiDao.insertReiki(defaultReiki);
//
//        // Insert 7 positions
//        String titles[] = {"Crown", "Third Eye", "Throat", "Heart", "Solar Plexus", "Sacral", "Root"};
//
//        for (int i = 0; i < titles.length; i++) {
//            Position position = new Position(
//                    defaultReiki.getId(),
//                    i+1,
//                    titles[i],
//                    "3:15"
//            );
//
//            reikiDao.insertPosition(position);
//        }
//    }

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
//        reikiDao.deleteReiki(reiki);
//
//        int seqNo = reiki.getSeqNo();
//
//        //Get all Reikis with seqNo greater than deleted Reiki
//        List<Reiki> reikiList = reikiDao.getReikisGreaterThanSeqNo(seqNo);
//
//        for (Reiki tempReiki: reikiList){
//            //decrement by 1
//            tempReiki.setSeqNo(
//                    tempReiki.getSeqNo() - 1
//            );
//
//            //may be easier/faster to do bulk update but I'm not certain
//            // TODO: move this to outside for loop and pass array
//            reikiDao.updateReiki(tempReiki);
//        }
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
