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

import androidx.lifecycle.ViewModel;
import android.os.AsyncTask;

import com.learnteachcenter.ltcreikiclock.data.Position;
import com.learnteachcenter.ltcreikiclock.data.Reiki;
import com.learnteachcenter.ltcreikiclock.data.source.ReikiRepository;

/**
 * Created by R_KAY on 8/11/2017.
 */

public class NewReikiViewModel extends ViewModel {

    private ReikiRepository repository;

    NewReikiViewModel(ReikiRepository repository) {
        this.repository = repository;
    }

    /**
     * Attach our LiveData to the Database
     */
    public void addReikiToDatabase(Reiki reiki){
        new AddReikiTask().execute(reiki);
    }
    public void updateReikiInDatabase(Reiki reiki) { new UpdateReikiTask().execute(reiki); }
    public void addPositionToDatabase(Position position) { new AddPositionTask().execute(position); }
    public void updatePositionInDatabase(Position position) { new UpdatePositionTask().execute(position); }

    private class AddReikiTask extends AsyncTask<Reiki, Void, Void> {

        @Override
        protected Void doInBackground(Reiki... reiki) {
            repository.createNewReiki(reiki[0]);
            return null;
        }
    }

    private class UpdateReikiTask extends AsyncTask<Reiki, Void, Void> {

        @Override
        protected Void doInBackground(Reiki... reiki){
            repository.updateReiki(reiki[0]);
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

    private class UpdatePositionTask extends AsyncTask<Position, Void, Void> {

        @Override
        protected Void doInBackground(Position...position) {
            repository.updatePosition(position[0]);
            return null;
        }
    }
}
