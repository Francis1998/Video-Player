package main.java.presenter.display;

import main.java.presenter.base.BasePresenter;
import main.java.view.display.VideoDisplayView;

public class VideoDisplayPresenter extends BasePresenter {
    public VideoDisplayView mVideoDisplayView;
    public VideoDisplayPresenter(){
        super();
    }
    public VideoDisplayPresenter(VideoDisplayView videoDisplayView){
        this();
        this.mVideoDisplayView = videoDisplayView;
    }
}
