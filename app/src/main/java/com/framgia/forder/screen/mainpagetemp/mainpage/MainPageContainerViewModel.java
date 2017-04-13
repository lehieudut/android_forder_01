package com.framgia.forder.screen.mainpagetemp.mainpage;

/**
 * Exposes the data to be used in the Main screen.
 */

public class MainPageContainerViewModel implements MainPageContainerContract.ViewModel {

    private MainPageContainerContract.Presenter mPresenter;
    private MainPageContainerAdapter mAdapter;

    public MainPageContainerViewModel(MainPageContainerAdapter adapter) {
        mAdapter = adapter;
    }

    @Override
    public void onStart() {
        mPresenter.onStart();
    }

    @Override
    public void onStop() {
        mPresenter.onStop();
    }

    @Override
    public void setPresenter(MainPageContainerContract.Presenter presenter) {
        mPresenter = presenter;
    }
}