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
    TimerTask task;
    int number = 9000;
    long period = 30L;
    long delay = 0;
    boolean isPause = false;
    boolean isStop = false;
    public ActionPresenter(ActionView view){
        super();
        setView(view);
        timer = new Timer();
    }
    public void setView(ActionView view){ mView = view; }

    public void onOpenFile(File file) {
        DataManager.getInstance().getLinkListByFile(file.getAbsolutePath());
        DataManager.getInstance().setPrimaryVideo(DataManager.getInstance().LinkData.get(0).sourceFilePathBase);
        DataManager.getInstance().initAudio();
        EventBusCenter.post(new PrimarySlideEvent(1));
    }

    @Subscribe
    public void timerStart(StartEvent event){
        if (timer != null){
            isPause = false;
            isStop = false;
        }
        schedule();
//        timer = new Timer();
//        task = new TimerTask() {
//            //            int count = 1;
//            @Override
//            public void run() {
//                if (!isPause && !isStop) {
//                    if (DataManager.getInstance().currFrame <= number) {
//                        EventBusCenter.post(new PrimarySlideEvent(DataManager.getInstance().currFrame));
//                    } else {
//                        DataManager.getInstance().currFrame = 1;
//                        timer.cancel();
//                    }
//                    System.out.println(DataManager.getInstance().currFrame + "    " + (DataManager.getInstance().audio_play_line.getFramePosition() / (DataManager.getInstance().bytes_per_video_frame / 4) + DataManager.getInstance().audio_video_offset));
//                    // System.out.println("audio video offset: " + DataManager.getInstance().audio_video_offset);
//                    if (DataManager.getInstance().currFrame - (DataManager.getInstance().audio_play_line.getFramePosition() / (DataManager.getInstance().bytes_per_video_frame / 4) + DataManager.getInstance().audio_video_offset) >= 3) {
//                        delay = 100L;
//                        period = 110L;
//                        System.out.println("voice slow");
//                    } else if ((DataManager.getInstance().audio_play_line.getFramePosition() / (DataManager.getInstance().bytes_per_video_frame / 4) + DataManager.getInstance().audio_video_offset) - DataManager.getInstance().currFrame >= 1) {
//                        delay = 0L;
//                        period = 1L;
//                        System.out.println("voice fast");
//                    } else {
//                        delay = 0L;
//                        period = 38L;
//                        System.out.println("same voice");
//                    }
//                    System.out.println("delay: " + delay + "  period" + period);
////                System.out.println(period);
//                    DataManager.getInstance().currFrame++;
//                }
//            }
//        };
//       timer.schedule(task, delay, period);
//       timer.cancel();
    }
    public void schedule(){
        timer.schedule(new ScheduleTask(), delay);
    }
    public class ScheduleTask extends TimerTask{

        @Override
        public void run() {
            if (!isPause && !isStop) {
                if (DataManager.getInstance().currFrame <= number) {
                    EventBusCenter.post(new PrimarySlideEvent(DataManager.getInstance().currFrame));
                } else {
                    DataManager.getInstance().currFrame = 1;
                    timer.cancel();
                }
                System.out.println(DataManager.getInstance().currFrame + "    " + (DataManager.getInstance().audio_play_line.getFramePosition() / (DataManager.getInstance().bytes_per_video_frame / 4) + DataManager.getInstance().audio_video_offset));
                // System.out.println("audio video offset: " + DataManager.getInstance().audio_video_offset);
                if (DataManager.getInstance().currFrame - (DataManager.getInstance().audio_play_line.getFramePosition() / (DataManager.getInstance().bytes_per_video_frame / 4) + DataManager.getInstance().audio_video_offset) >= 3) {
                    delay = 40L;
                    System.out.println("voice slow");
                } else if ((DataManager.getInstance().audio_play_line.getFramePosition() / (DataManager.getInstance().bytes_per_video_frame / 4) + DataManager.getInstance().audio_video_offset) - DataManager.getInstance().currFrame >= 1) {
                    delay = 20L;
                    System.out.println("voice fast");
                } else {
                    delay = 30L;
                    System.out.println("same voice");
                }
                System.out.println("delay: " + delay + "  period" + period);
                DataManager.getInstance().currFrame++;
                schedule();
            }
        }
    }

    @Subscribe
    public void timerPause(PauseEvent event){
//        if (timer != null){
//            timer.cancel();
//        }
        isPause = true;
    }

    @Subscribe
    public void timerStop(StopEvent event){
//        if (timer != null){
//
//            timer.cancel();
//
//
//        }
        isStop = true;
        DataManager.getInstance().currFrame = 1;
        delay = 0L;
        period = 33L;
        EventBusCenter.post(new PrimarySlideEvent(DataManager.getInstance().currFrame));
    }
}
