package com.riders.thelab.ui.contacts;

import com.riders.thelab.di.scopes.ActivityScope;
import com.riders.thelab.ui.base.BasePresenter;

import dagger.Binds;
import dagger.Module;
import dagger.Provides;

@Module
public abstract class ContactsModule {

    @ActivityScope
    @Provides
    static ContactsContract.View provideView(ContactsActivity activity) {
        return new ContactsView(activity);
    }

    @ActivityScope
    @Provides
    static Navigator provideNavigator(ContactsActivity activity) {
        return new Navigator(activity);
    }

    @ActivityScope
    @Binds
    abstract BasePresenter providePresenter(ContactsPresenter presenter);

}
