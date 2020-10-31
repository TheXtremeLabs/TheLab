package com.riders.thelab.ui.base;

public class BasePresenterImpl<T extends BaseView> implements BasePresenter<T> {

    private T view;

    public T getView() {
        return view;
    }

    @Override
    public void attachView(T view) {
        this.view = view;
    }

    @Override
    public void detachView() {
        view = null;
    }

}