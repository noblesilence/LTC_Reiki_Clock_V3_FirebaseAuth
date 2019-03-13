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

package com.learnteachcenter.ltcreikiclock.dependencyinjection;

import android.app.Application;
import android.arch.lifecycle.ViewModelProvider;
import android.arch.persistence.room.Room;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

import com.learnteachcenter.ltcreikiclock.Utils;
import com.learnteachcenter.ltcreikiclock.data.source.local.ReikiDao;
import com.learnteachcenter.ltcreikiclock.data.source.local.ReikiDatabase;
import com.learnteachcenter.ltcreikiclock.data.source.ReikiRepository;
import com.learnteachcenter.ltcreikiclock.viewmodel.CustomViewModelFactory;

/**
 * Created by R_KAY on 8/18/2017.
 */
@Module
public class RoomModule {

    private final ReikiDatabase database;

    public RoomModule(Application application) {
        this.database = Room.databaseBuilder(
                    application,
                    ReikiDatabase.class,
                    "Reiki.db"
                )
                .build();
    }

    @Provides
    @Singleton
    ReikiDao provideReikiDao(ReikiDatabase database){
        return database.reikiDao();
    }

    @Provides
    @Singleton
    ReikiDatabase provideReikiDatabase(Application application){
        return database;
    }

    @Provides
    @Singleton
    ViewModelProvider.Factory provideViewModelFactory(ReikiRepository repository){
        return new CustomViewModelFactory(repository);
    }

    @Provides
    @Singleton
    Utils provideUtils(Application application) {
        return new Utils(application);
    }
}
