package com.riders.thelab.ui.contacts.addcontact;

import com.riders.thelab.di.scopes.ActivityScope;
import com.riders.thelab.navigator.Navigator;
import com.riders.thelab.ui.base.BasePresenter;

import dagger.Binds;
import dagger.Module;
import dagger.Provides;

@Module
public abstract class AddContactModule {

    @ActivityScope
    @Provides
    static AddContactContract.View provideView(AddContactActivity activity) {
        return new AddContactView(activity);
    }

    @ActivityScope
    @Provides
    static Navigator provideNavigator(AddContactActivity activity) {
        return new Navigator(activity);
    }

    @ActivityScope
    @Binds
    abstract BasePresenter providePresenter(AddContactPresenter presenter);

}
