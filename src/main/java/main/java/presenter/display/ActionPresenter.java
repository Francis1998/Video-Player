package main.java.presenter.display;

import com.google.common.eventbus.Subscribe;
import main.java.constants.LiteralConstants;
import main.java.data.DataManager;
import main.java.event.PauseEvent;
import main.java.event.PrimarySlideEvent;
import main.java.event.StartEvent;
import main.java.event.StopEvent;
import main.java.eventbus.EventBusCenter;
import main.java.presenter.base.BasePresenter;
import main.java.view.toolbar.ActionView;

import java.io.File;
import java.util.Timer;
import java.util.TimerTask;

public class ActionPresenter extends BasePresenter {
    ActionView mView = null;
    Timer timer;
    TimerTask task;
    int number = 9000;
    long period = 30L;
    long delay = 0;
    boolean isPause = false;
    boolean isStop = false;

    public ActionPresenter(ActionView view) {
        super();
        setView(view);
        timer = new Timer();
    }

    public void setView(ActionView view) {
        mView = view;
    }

    public void onOpenFile(File file) {
        DataManager.getInstance().getLinkListByFile(file.getAbsolutePath());

        DataManager.getInstance().initAudio();
        EventBusCenter.post(new PrimarySlideEvent(1));
    }

    @Subscribe
    public void timerStart(StartEvent event) {
        if (timer != null) {
            isPause = false;
            isStop = false;
        }
        schedule();
    }

    public void schedule() {
        timer.schedule(new ScheduleTask(), delay);
    }

    @Subscribe
    public void timerPause(PauseEvent event) {
        isPause = true;
    }

    @Subscribe
    public void timerStop(StopEvent event) {
        isStop = true;
        DataManager.getInstance().currFrame = 1;
        delay = 0L;
        period = 33L;
        EventBusCenter.post(new PrimarySlideEvent(DataManager.getInstance().currFrame));
    }

    public class ScheduleTask extends TimerTask {

        @Override
        public void run() {
            if (!isPause && !isStop) {
                if (DataManager.getInstance().currFrame <= number) {
                    EventBusCenter.post(new PrimarySlideEvent(DataManager.getInstance().currFrame));
                } else {
                    DataManager.getInstance().currFrame = 1;
                    timer.cancel();
                }
                // System.out.println("audio video offset: " + DataManager.getInstance().audio_video_offset);
                videoSpeedFeedback();
                DataManager.getInstance().currFrame++;
                schedule();
            }
        }
    }

    public void videoSpeedFeedback(){
        int diffSpeed = DataManager.getInstance().getDiffBetweenVideoAndAudio();
        if (diffSpeed >= LiteralConstants.videoQuickThreshold) {
            delay = Math.round((diffSpeed * 0.1 + 1) * LiteralConstants.videoBasicDelay);
        } else if (diffSpeed <= LiteralConstants.audioQuickThreshold) {
            delay = Math.round(LiteralConstants.videoBasicDelay/(Math.abs(diffSpeed) * 0.1 + 1));
        } else {
            delay = LiteralConstants.videoBasicDelay;
        }
    }

}
