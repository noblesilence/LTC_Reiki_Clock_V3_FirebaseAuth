/*
 * *
 *  * Copyright (C) 2017 Ryan Kay Open Source Project
 *  *
 *  * Licensed under the Apache License, Version 2.0 (the "License");
 *  * you may not use this file except in compliance with the License.
 *  * You may obtain a copy of the License at
 *  *
 *  *      http://www.apache.org/licenses/LICENSE-2.0
 *  *
 *  * Unless required by applicable law or agreed to in writing, software
 *  * distributed under the License is distributed on an "AS IS" BASIS,
 *  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  * See the License for the specific language governing permissions and
 *  * limitations under the License.
 *
 */

package com.learnteachcenter.ltcreikiclock.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import android.os.AsyncTask;
import android.util.Log;

import java.util.List;
import java.util.concurrent.TimeUnit;

import com.learnteachcenter.ltcreikiclock.data.Position;
import com.learnteachcenter.ltcreikiclock.data.Reiki;
import com.learnteachcenter.ltcreikiclock.data.source.ReikiRepository;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;


/**
 * Created by aye2m on 10/14/17.
 *
 * Credit:
 * https://www.youtube.com/playlist?list=PLEVlop6sMHCpClb5MoCSChoufFvax9lDO
 *
 */

public class ReikiCollectionViewModel extends ViewModel {

    private static final String TAG = "Reiki";

    // Repository
    private ReikiRepository repository;

    private MutableLiveData<List<Reiki>> reikisResult = new MutableLiveData<>();
    private MutableLiveData<String> reikisError = new MutableLiveData<>();
    private MutableLiveData<Boolean> reikisLoader = new MutableLiveData<>();
    private DisposableObserver<List<Reiki>> disposableObserver;

    // Constructor
    ReikiCollectionViewModel(ReikiRepository repository) {
        this.repository = repository;
    }

    public LiveData<List<Reiki>> reikisResult() {
        return reikisResult;
    }

    public LiveData<String> reikisError() {
        return reikisError;
    }

    public LiveData<Boolean> reikisLoader(){
        return reikisLoader;
    }

    public void loadReikis() {
        DisposableObserver<List<Reiki>> disposableObserver = new
                DisposableObserver<List<Reiki>>() {
                    @Override
                    public void onNext(List<Reiki> reikis) {
                        reikisResult.postValue(reikis);
                        reikisLoader.postValue(false);
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.d(TAG, "[ReikiCollectionViewModel] onError: " + e.getMessage());
                        reikisError.postValue(e.getMessage());
                        reikisLoader.postValue(false);
                    }

                    @Override
                    public void onComplete() {

                    }
                };

        repository.getListOfReikis()
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .debounce(400, TimeUnit.MILLISECONDS)
                .subscribe(disposableObserver);
    }

    public void disposeElements(){
        if( disposableObserver != null &&
        !disposableObserver.isDisposed()) {
            disposableObserver.dispose();
        }
    }

    // Reikis
    public Observable<List<Reiki>> getReikis() {
        return repository.getListOfReikis();
    }

    //See repository for implementation
    public void populateReikiDatabase() {
        new PopulateReikiDatabaseTask().execute();
    }

    private class PopulateReikiDatabaseTask extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... voids) {
//            repository.populateReikiDatabase();
            return null;
        }
    }


    public void deleteReiki(Reiki reiki) {
        DeleteReikiTask deleteReikiTask = new DeleteReikiTask();
        deleteReikiTask.execute(reiki);
    }

    private class DeleteReikiTask extends AsyncTask<Reiki, Void, Void> {

        @Override
        protected Void doInBackground(Reiki... reiki) {
            repository.deleteReiki(reiki[0]);
            return null;
        }
    }

    // Update Reikis
    public void updateReikis(Reiki... reikis) {
        UpdateReikisTask updateReikisTask = new UpdateReikisTask();
        updateReikisTask.execute(reikis);
    }

    private class UpdateReikisTask extends AsyncTask<Reiki, Void, Void> {
        @Override
        protected Void doInBackground(Reiki... reikis){
            repository.updateReikis(reikis);
            return null;
        }
    }

    // Update Reikis
    public void updatePositions(Position... positions) {
        UpdatePositionsTask updatePositionsTask = new UpdatePositionsTask();
        updatePositionsTask.execute(positions);
    }

    private class UpdatePositionsTask extends AsyncTask<Position, Void, Void> {
        @Override
        protected Void doInBackground(Position... positions){
            repository.updatePositions(positions);
            return null;
        }
    }

    // Positions

    public LiveData<List<Position>> getPositions(String reikiId) { return repository.getListOfPositionsByReikiId(reikiId); }

    public void deletePosition(Position position) {
        DeletePositionTask deletePositionTask = new DeletePositionTask();
        deletePositionTask.execute(position);
    }

    private class DeletePositionTask extends AsyncTask<Position, Void, Void> {

        @Override
        protected  Void doInBackground(Position... position) {
            repository.deletePosition(position[0]);
            return null;
        }
    }

//    public void rearrangePosition(String reikiId,
//                                  String initialPositionSeqNo,
//                                  String finalPositionSeqNo) {
//        RearrangePositionTask rearrangePositionTask = new RearrangePositionTask();
//        rearrangePositionTask.execute(reikiId, initialPositionSeqNo, finalPositionSeqNo);
//    }
//
//    private class RearrangePositionTask extends AsyncTask<String, Void, Void> {
//
//        @Override
//        protected Void doInBackground(String... position) {
//            //0: Reiki Id, 1: initial, 2: final
//            repository.rearrangePosition(position[0], position[1], position[2]);
//            return null;
//        }
//
//    }

    public void addReikiToDatabase(Reiki reiki){
        new ReikiCollectionViewModel
                .AddReikiTask().execute(reiki);
    }
    public void addPositionToDatabase(Position position) { new ReikiCollectionViewModel
            .AddPositionTask().execute(position); }

    private class AddReikiTask extends AsyncTask<Reiki, Void, Void> {

        @Override
        protected Void doInBackground(Reiki... reiki) {
            repository.createNewReiki(reiki[0]);
            return null;
        }
    }

    private class AddPositionTask extends AsyncTask<Position, Void, Void> {

        @Override
        protected Void doInBackground(Position... position) {
            repository.createNewPosition(position[0]);
            return null;
        }
    }

}

