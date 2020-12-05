package com.riders.thelab.di.module;

import com.riders.thelab.di.scopes.ActivityScope;
import com.riders.thelab.ui.filterlistview.FilterListViewActivity;
import com.riders.thelab.ui.filterlistview.FilterListViewModule;
import com.riders.thelab.ui.mainactivity.MainActivity;
import com.riders.thelab.ui.mainactivity.MainActivityModule;
import com.riders.thelab.ui.palette.PaletteActivity;
import com.riders.thelab.ui.palette.PaletteModule;
import com.riders.thelab.ui.splashscreen.SplashScreenActivity;
import com.riders.thelab.ui.splashscreen.SplashScreenModule;

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
    @ContributesAndroidInjector(modules = PaletteModule.class)
    abstract PaletteActivity paletteActivity();

    @ActivityScope
    @ContributesAndroidInjector(modules = FilterListViewModule.class)
    abstract FilterListViewActivity filterListViewActivity();
}


