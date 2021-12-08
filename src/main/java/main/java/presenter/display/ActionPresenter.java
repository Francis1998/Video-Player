package main.java.presenter.display;

import com.google.common.eventbus.Subscribe;
import main.java.data.DataManager;
import main.java.event.PauseEvent;
import main.java.event.PrimarySlideEvent;
import main.java.event.StartEvent;
import main.java.event.StopEvent;
import main.java.eventbus.EventBusCenter;
import main.java.presenter.base.BasePresenter;
import main.java.view.toolbar.ActionView;
import java.util.Timer;
import java.io.File;
import java.util.TimerTask;

public class ActionPresenter extends BasePresenter {
    ActionView mView = null;
    Timer timer;
//    int count = 1;
    int number = 9000;

    public ActionPresenter(ActionView view){
        super();
        setView(view);
    }
    public void setView(ActionView view){ mView = view; }

    public void onOpenFile(File file) {
        DataManager.getInstance().getLinkListByFile(file.getAbsolutePath());
        DataManager.getInstance().setPrimaryVideo(DataManager.getInstance().LinkData.get(0).sourceFilePathBase);
        DataManager.getInstance().initAudio();
        EventBusCenter.post(new PrimarySlideEvent(1));
//        } else {
//            DataManager.getInstance().openSecondaryVideo(file.getAbsolutePath());
//            EventBusCenter.post(new SecondarySlideEvent(type, 1));
//        }
    }
    @Subscribe
    public void timerStart(StartEvent event){
        if (timer != null){
            timer.cancel();
        }
        timer = new Timer();
        TimerTask task = new TimerTask() {
//            int count = 1;
            @Override
            public void run() {
                if(DataManager.getInstance().currFrame<=number){
//                    System.out.println(DataManager.getInstance().currFrame);
                    EventBusCenter.post(new PrimarySlideEvent(DataManager.getInstance().currFrame));
                } else {
                    DataManager.getInstance().currFrame = 1;
                    timer.cancel();
                }
                DataManager.getInstance().currFrame++;
            }
        };
        timer.schedule(task, 0,33L);
    }

    @Subscribe
    public void timerPause(PauseEvent event){
        if (timer != null){
            timer.cancel();
        }
    }

    @Subscribe
    public void timerStop(StopEvent event){
        if (timer != null){
            DataManager.getInstance().currFrame = 1;
            timer.cancel();
            EventBusCenter.post(new PrimarySlideEvent(DataManager.getInstance().currFrame));
        }
    }
}
