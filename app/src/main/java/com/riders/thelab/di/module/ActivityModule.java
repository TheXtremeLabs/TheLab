package com.riders.thelab.di.module;

import com.riders.thelab.di.scopes.ActivityScope;
import com.riders.thelab.ui.contacts.ContactsActivity;
import com.riders.thelab.ui.contacts.ContactsModule;
import com.riders.thelab.ui.contacts.addcontact.AddContactActivity;
import com.riders.thelab.ui.contacts.addcontact.AddContactModule;
import com.riders.thelab.ui.deviceinformation.DeviceInformationActivity;
import com.riders.thelab.ui.deviceinformation.DeviceInformationModule;
import com.riders.thelab.ui.filterlistview.FilterListViewActivity;
import com.riders.thelab.ui.filterlistview.FilterListViewModule;
import com.riders.thelab.ui.locationonmaps.LocationOnMapsActivity;
import com.riders.thelab.ui.locationonmaps.LocationOnMapsModule;
import com.riders.thelab.ui.mainactivity.MainActivity;
import com.riders.thelab.ui.mainactivity.MainActivityModule;
import com.riders.thelab.ui.multipane.MultipaneActivity;
import com.riders.thelab.ui.multipane.MultipaneModule;
import com.riders.thelab.ui.palette.PaletteActivity;
import com.riders.thelab.ui.palette.PaletteModule;
import com.riders.thelab.ui.recycler.RecyclerViewActivity;
import com.riders.thelab.ui.recycler.RecyclerViewModule;
import com.riders.thelab.ui.schedule.ScheduleActivity;
import com.riders.thelab.ui.schedule.ScheduleModule;
import com.riders.thelab.ui.splashscreen.SplashScreenActivity;
import com.riders.thelab.ui.splashscreen.SplashScreenModule;
import com.riders.thelab.ui.weather.WeatherActivity;
import com.riders.thelab.ui.weather.WeatherModule;
import com.riders.thelab.ui.youtubelike.YoutubeLikeActivity;
import com.riders.thelab.ui.youtubelike.YoutubeLikeModule;

import dagger.Module;
import dagger.android.ContributesAndroidInjector;

@Module
public abstract class ActivityModule {

    @ActivityScope
    @ContributesAndroidInjector(modules = SplashScreenModule.class)
    abstract SplashScreenActivity splashScreenActivity();

    @ActivityScope
    @ContributesAndroidInjector(modules = MainActivityModule.class)
    abstract MainActivity mainActivity();

    @ActivityScope
    @ContributesAndroidInjector(modules = ContactsModule.class)
    abstract ContactsActivity contactsActivity();

    @ActivityScope
    @ContributesAndroidInjector(modules = AddContactModule.class)
    abstract AddContactActivity addContactActivity();

    @ActivityScope
    @ContributesAndroidInjector(modules = LocationOnMapsModule.class)
    abstract LocationOnMapsActivity locationOnMapsActivity();

    @ActivityScope
    @ContributesAndroidInjector(modules = RecyclerViewModule.class)
    abstract RecyclerViewActivity recyclerViewActivity();

    @ActivityScope
    @ContributesAndroidInjector(modules = ScheduleModule.class)
    abstract ScheduleActivity scheduleActivity();

    @ActivityScope
    @ContributesAndroidInjector(modules = DeviceInformationModule.class)
    abstract DeviceInformationActivity deviceInformationActivity();

    @ActivityScope
    @ContributesAndroidInjector(modules = PaletteModule.class)
    abstract PaletteActivity paletteActivity();

    @ActivityScope
    @ContributesAndroidInjector(modules = FilterListViewModule.class)
    abstract FilterListViewActivity filterListViewActivity();


    @ActivityScope
    @ContributesAndroidInjector(modules = MultipaneModule.class)
    abstract MultipaneActivity multipaneActivity();


    @ActivityScope
    @ContributesAndroidInjector(modules = YoutubeLikeModule.class)
    abstract YoutubeLikeActivity youtubeLikeActivity();

    @ActivityScope
    @ContributesAndroidInjector(modules = WeatherModule.class)
    abstract WeatherActivity weatherActivity();

}


