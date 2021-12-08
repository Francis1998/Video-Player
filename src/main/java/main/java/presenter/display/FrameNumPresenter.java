package main.java.presenter.display;

import com.google.common.eventbus.Subscribe;
import main.java.data.DataManager;
import main.java.event.PrimarySlideEvent;
import main.java.presenter.base.BasePresenter;
import main.java.view.display.FrameNumView;

public class FrameNumPresenter extends BasePresenter {
    FrameNumView mView;

    public FrameNumPresenter(FrameNumView view){
        super();
        mView = view;
    }

    public void setView(FrameNumView view){
        mView = view;
    }
    @Subscribe
    public void init_load(PrimarySlideEvent event) {
//        System.out.println("update" + event.newValue);
        this.mView.updateNum(event.newValue);
//        String filename = ;
//        showRGB(filename);
    }
}
