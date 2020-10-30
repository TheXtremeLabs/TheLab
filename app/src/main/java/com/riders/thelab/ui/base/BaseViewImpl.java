package com.riders.thelab.ui.base;

import javax.inject.Inject;

public abstract class BaseViewImpl<P extends BasePresenter> {

    @Inject
    P presenter;

    public P getPresenter() {
        return presenter;
    }

}
