package main.java.presenter.display;

import com.google.common.eventbus.Subscribe;
import main.java.data.DataManager;
import main.java.event.*;
import main.java.presenter.base.BasePresenter;
import main.java.task.AudioTask;
import main.java.view.display.PrimaryVideoDisplayView;

public class PrimaryVideoDisplayPresenter extends BasePresenter {
    PrimaryVideoDisplayView mView;
    public Thread thread;

    public PrimaryVideoDisplayPresenter() {
        super();
    }

    public boolean isPause = false;
    public boolean isInitiated = false;
    public boolean isStopped = false;
    public boolean isJumped = false;
    public JumpEvent jump_event;

    public PrimaryVideoDisplayPresenter(PrimaryVideoDisplayView view) {
        this();
        setView(view);
        thread = new Thread(new AudioTask(this));
    }

    public void setView(PrimaryVideoDisplayView view) {
        mView = view;
    }

    @Subscribe
    public void init_load(PrimarySlideEvent event) {
        String filename = DataManager.getInstance().getFilenameByFrameNo(event.newValue);
        DataManager.getInstance().currFrame = event.newValue;
        this.mView.showRGB(filename);
    }

    @Subscribe
    public void soundOnStart(StartEvent event) {
        if (!isInitiated) {
            thread.start();
            isInitiated = true;
        }
        isPause = false;
    }

    @Subscribe
    public void soundOnPause(PauseEvent event) {
        isPause = true;
    }

    @Subscribe
    public void soundOnStop(StopEvent event) {
        isStopped = true;
        isPause = true;
    }

    @Subscribe
    public void soundOnJump(JumpEvent event) {
        isJumped = true;
        jump_event = event;
    }

    public void onMouseClicked() {

    }
}
