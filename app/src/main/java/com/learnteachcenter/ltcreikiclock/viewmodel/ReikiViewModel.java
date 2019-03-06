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

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModel;

import com.learnteachcenter.ltcreikiclock.data.Reiki;
import com.learnteachcenter.ltcreikiclock.data.ReikiRepository;

import java.util.UUID;

/**
 * Created by R_KAY on 8/3/2017.
 */

public class ReikiViewModel extends ViewModel {

    private ReikiRepository repository;

    ReikiViewModel(ReikiRepository repository) {
        this.repository = repository;
    }

    public LiveData<Reiki> getReikiById(String reikiId){
       return repository.getReiki(reikiId);
    }

}

