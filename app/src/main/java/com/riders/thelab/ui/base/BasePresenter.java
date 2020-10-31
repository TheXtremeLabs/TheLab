package com.riders.thelab.ui.base;

public interface BasePresenter<T extends BaseView> {

    void attachView(T view);

    void detachView();
}
