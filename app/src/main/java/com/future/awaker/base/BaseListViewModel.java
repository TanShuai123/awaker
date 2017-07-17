package com.future.awaker.base;

import android.databinding.Observable;
import android.databinding.ObservableBoolean;

/**
 * Copyright ©2017 by Teambition
 */

public abstract class BaseListViewModel extends BaseViewModel {

    protected static final int NORMAL_PAGE = 1;

    protected int page;
    protected boolean isRefresh;

    public ObservableBoolean refreshing = new ObservableBoolean(false); // 提供给下拉刷新

    private RunCallBack runCallBack = new RunCallBack();

    public BaseListViewModel() {
        isRunning.addOnPropertyChangedCallback(runCallBack);
    }

    public abstract void refreshData(boolean refresh);

    public void fetchData(boolean refresh) {
        if (refresh) {
            if (isRunning.get()) {
                refreshing.set(false);
                return;
            }
            page = NORMAL_PAGE;

        } else {
            if (isRunning.get() || isEmpty.get()) {
                return;
            }
            page++;
        }

        setRefresh(refresh);
        refreshData(refresh);
    }

    public void setRefresh(boolean refresh) {
        isRefresh = refresh;
        if (isRefresh) { // 网络请求开始显示下拉刷新
            refreshing.set(true);
        }
    }

    @Override
    public void clear() {
        isRunning.removeOnPropertyChangedCallback(runCallBack);
        super.clear();
    }

    private class RunCallBack extends OnPropertyChangedCallback {

        @Override
        public void onPropertyChanged(Observable sender, int propertyId) {
            if (!isRunning.get()) { // 网络请求结束隐藏下拉刷新
                refreshing.set(false);
            }
        }
    }
}
