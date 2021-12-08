package main.java.presenter.display;

import com.google.common.eventbus.Subscribe;
import main.java.data.DataManager;
import main.java.event.PrimarySlideEvent;
import main.java.presenter.base.BasePresenter;
import main.java.view.display.PrimaryVideoDisplayView;

public class PrimaryVideoDisplayPresenter extends BasePresenter {
    PrimaryVideoDisplayView mView;
    long startTime;
    public PrimaryVideoDisplayPresenter() {
        super();
    }

    public PrimaryVideoDisplayPresenter(PrimaryVideoDisplayView view) {
        this();
        setView(view);
    }

    public void setView(PrimaryVideoDisplayView view) {
        mView = view;
    }

    @Subscribe
    public void init_load(PrimarySlideEvent event) {
        String filename = DataManager.getInstance().getFilenameByFrameNo(event.newValue);
//        if (event.newValue == 1){
//            startTime=System.currentTimeMillis();
//        } else if (event.newValue == 30){
//            System.out.println(System.currentTimeMillis() - startTime);
//        }
        this.mView.showRGB(filename);
//        String filename = ;
//        showRGB(filename);
    }

    public void onMouseClicked() {

    }
}
