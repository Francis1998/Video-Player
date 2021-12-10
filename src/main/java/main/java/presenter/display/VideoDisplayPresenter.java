package main.java.presenter.display;

import com.google.common.eventbus.Subscribe;
import main.java.data.DataManager;
import main.java.event.PrimarySlideEvent;
import main.java.event.ShowEvent;
import main.java.presenter.base.BasePresenter;
import main.java.view.display.VideoDisplayView;

public class VideoDisplayPresenter extends BasePresenter {
    public VideoDisplayView mVideoDisplayView;

    public VideoDisplayPresenter() {
        super();
    }

    public VideoDisplayPresenter(VideoDisplayView videoDisplayView) {
        this();
        this.mVideoDisplayView = videoDisplayView;
    }

    @Subscribe
    public void showBoundingbox(ShowEvent event) {
        mVideoDisplayView.showBoundingbox(event);
    }
    @Subscribe
    public void continueRefreshBoundingBox(PrimarySlideEvent event){
        mVideoDisplayView.continueRefreshBoundingBox(event);
    }
}
