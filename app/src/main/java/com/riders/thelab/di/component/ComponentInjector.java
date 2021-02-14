package com.riders.thelab.di.component;

import android.app.Application;

import com.riders.thelab.TheLabApplication;
import com.riders.thelab.data.DataModule;
import com.riders.thelab.di.module.ActivityModule;
import com.riders.thelab.di.module.ApplicationModule;

import javax.inject.Singleton;

import dagger.BindsInstance;
import dagger.Component;
import dagger.android.AndroidInjector;
import dagger.android.support.AndroidSupportInjectionModule;

@Singleton
@Component(modules = {
        AndroidSupportInjectionModule.class,
        ApplicationModule.class,
        DataModule.class,
        ActivityModule.class
})
public interface ComponentInjector extends AndroidInjector<TheLabApplication> {

    /*@Component.Factory
    interface Factory {
        @BindsInstance
        ComponentInjector create(Context context);
    }*/

    @Component.Builder
    interface Builder {
        @BindsInstance
        ComponentInjector.Builder application(Application application);

        ComponentInjector.Builder applicationModule(ApplicationModule applicationModule);

        ComponentInjector.Builder dataModule(DataModule dataModule);

        ComponentInjector build();
    }
}
