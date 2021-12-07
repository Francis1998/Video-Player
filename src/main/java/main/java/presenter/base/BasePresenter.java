package main.java.presenter.base;

import main.java.eventbus.EventBusCenter;

public class BasePresenter implements IBasePresenter {
    public BasePresenter() {
        EventBusCenter.register(this);
    }
}
