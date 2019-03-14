/*
 * LTC Reiki Clock (v 3.0)
 * Created by aye2m on 10/17/207.
 * Developed by Aye Aye Mon
 * 2017-2019
 *
 * ACKNOWLEDGEMENT
 * Special Thanks to Ryan Kay, Darel Bitsy, Davor Maric, Renat Kaitmazov
 * for helping me in this project.
 *
 * DONE
 * 1. Save the data in DB (DONE)
 * 3. Auto focus dialogs (DONE)
 * 4. Change app name (DONE)
 * 5. Add play/pause/stop buttons to ReikiActivity (DONE)
 * 6. Let reordering (DONE)
 * 7. Cascade delete (DONE)
 * 8. Let Reiki info update
 * 9. Let Position info update
 * 10. Add undo in delete
 * 11. After reorder, update serial numbers
 *
 * REFERENCES
 * Let's Build A RecyclerView App | Beginner Android
 * https://www.youtube.com/watch?v=RfTJ2SePYaU&list=PLEVlop6sMHCohK2XryPUqhglysv1Xxakt
 *
 * Let's Build a Room Database App | Room, ViewModel, LiveData, Dagger 2, MVVM Architecture
 * https://www.youtube.com/playlist?list=PLEVlop6sMHCpClb5MoCSChoufFvax9lDO
 *
 * Android Studio Tutorials - Gradle Flavours (https://www.youtube.com/watch?v=7JDEK4wkN5I)
 * https://stackoverflow.com/questions/45376492/room-attempt-to-re-open-an-already-closed-database *
 * http://developer.android.com/training/material/lists-cards.html
 * http://stackoverflow.com/questions/2198410/how-to-change-title-of-activity-in-android
 * http://stackoverflow.com/questions/4233873/how-do-i-get-extra-data-from-intent-on-android
 * http://stackoverflow.com/questions/26466877/how-to-create-context-menu-for-recyclerview
 * http://stackoverflow.com/questions/27945078/onlongitemclick-in-recyclerview
 * http://stackoverflow.com/questions/9853430/refresh-fragment-when-dialogfragment-is-dismissed
 * http://stackoverflow.com/questions/19465049/changing-api-level-android-studio
 * http://stackoverflow.com/questions/35394181/switching-between-view-mode-and-edit-mode-in-android
 * http://stackoverflow.com/questions/24079960/making-a-custom-dialog-undismissable-in-android
 * https://stackoverflow.com/questions/18830205/application-crash-with-android-app-application-cannot-be-cast-to
 *
 *
 * Version 3
 * Offline first Android App with MVVM, Dagger2, RxJava, LiveData and Room
 * https://medium.com/@cdmunoz/offline-first-android-app-with-mvvm-dagger2-rxjava-livedata-and-room-25de4e1ada14
 *
 * Introduction to MVVM on Android - Tutorial - Learn Android Architecture Patterns
 * https://youtu.be/_T4zjIEkGOM
 *
 * Dagger 2 Beginner Tutorial - Android Programming
 * https://www.youtube.com/playlist?list=PLrnPJCHvNZuA2ioi4soDZKz8euUQnJW65
 *
 * Android Introduction To Reactive Programming – RxJava, RxAndroid
 * https://www.androidhive.info/RxJava/android-getting-started-with-reactive-programming/
 *
 * https://stackoverflow.com/questions/43853112/how-to-serialize-arraylistfloat-using-moshi-json-library-for-android
 *
 * */

package com.learnteachcenter.ltcreikiclock;

import android.app.Application;

import com.learnteachcenter.ltcreikiclock.dependencyinjection.ApplicationComponent;
import com.learnteachcenter.ltcreikiclock.dependencyinjection.ApplicationModule;
import com.learnteachcenter.ltcreikiclock.dependencyinjection.DaggerApplicationComponent;
import com.learnteachcenter.ltcreikiclock.dependencyinjection.NetModule;
import com.learnteachcenter.ltcreikiclock.dependencyinjection.RoomModule;

public class ReikiApplication extends Application {
    private ApplicationComponent applicationComponent;

    @Override
    public void onCreate() {
        super.onCreate();

        applicationComponent = DaggerApplicationComponent
                .builder()
                .applicationModule(new ApplicationModule(this))
                .roomModule(new RoomModule(this))
                .netModule(new NetModule(BuildConfig.URL))
                .build();

    }

    public ApplicationComponent getApplicationComponent() {
        return applicationComponent;
    }
}