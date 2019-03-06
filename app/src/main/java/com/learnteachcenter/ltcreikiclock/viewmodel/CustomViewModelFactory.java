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

import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProvider;

import javax.inject.Inject;
import javax.inject.Singleton;

import com.learnteachcenter.ltcreikiclock.data.ReikiRepository;

/**
 * Created by R_KAY on 8/17/2017.
 */
@Singleton
public class CustomViewModelFactory implements ViewModelProvider.Factory {
    private final ReikiRepository repository;

    @Inject
    public CustomViewModelFactory(ReikiRepository repository) {
        this.repository = repository;
    }

    @Override
    public <T extends ViewModel> T create(Class<T> modelClass) {

        if (modelClass.isAssignableFrom(ReikiCollectionViewModel.class))
            return (T) new ReikiCollectionViewModel(repository);

        else if (modelClass.isAssignableFrom(ReikiViewModel.class))
            return (T) new ReikiViewModel(repository);

        else if (modelClass.isAssignableFrom(NewReikiViewModel.class))
            return (T) new NewReikiViewModel(repository);

        else {
            throw new IllegalArgumentException("ViewModel Not Found");
        }
    }
}
