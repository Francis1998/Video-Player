package main.java.presenter.display;

import main.java.presenter.base.BasePresenter;
import main.java.view.toolbar.ButtonControlView;

public class ButtonControlPresenter extends BasePresenter {
    ButtonControlView mView;

    public ButtonControlPresenter(ButtonControlView view) {
        super();
        setView(view);
    }

    public void setView(ButtonControlView view) {
        mView = view;
    }
}
