package main.java.presenter.display;

import com.google.common.eventbus.Subscribe;
import main.java.data.DataManager;
import main.java.event.*;
import main.java.presenter.base.BasePresenter;
import main.java.view.display.PrimaryVideoDisplayView;

import java.util.concurrent.Callable;
import java.util.concurrent.FutureTask;

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
    public AudioCallable callable;
    FutureTask<String> futureTask;

    public class AudioCallable implements Callable<String>{
        @Override
        public String call() throws Exception {
            int cur_off = 0;
            int len = DataManager.getInstance().audio_data.length;

            int off = 0;
            int step = DataManager.getInstance().bytes_per_video_frame * 15;

            while (true) {
                if (isJumped) {
                    DataManager.getInstance().initAudio();
                    DataManager.getInstance().audio_video_offset = jump_event.targetFrame - 1;
                    cur_off = (jump_event.targetFrame - 1) * (DataManager.getInstance().bytes_per_video_frame / 4);
                    off = 0;
                    isJumped = false;
                }

                if (isStopped) {
                    DataManager.getInstance().audio_play_line.close();
                    DataManager.getInstance().audio_video_offset = 0;
                    cur_off = 0;
                    off = 0;
                    isStopped = false;
                }

                if (isPause) {
                    DataManager.getInstance().audio_play_line.stop();
                } else {
                    if (!DataManager.getInstance().audio_play_line.isOpen()) {
                        try {
                            DataManager.getInstance().audio_play_line.open(DataManager.getInstance().audioFormat);
                        } catch (Exception e) {
                            System.out.println("Exception thrown:" + e);
                        }
                    }
                    DataManager.getInstance().audio_play_line.start();
                    if (cur_off + off < len) {
                        DataManager.getInstance().audio_play_line.write(DataManager.getInstance().audio_data, cur_off + off, step);
                        DataManager.getInstance().audio_play_line.drain();
                        off += step;
                    }
                }
            }
        }

    }
    public PrimaryVideoDisplayPresenter(PrimaryVideoDisplayView view) {
        this();
        setView(view);
        callable = new AudioCallable();
        futureTask = new FutureTask<String>(callable);

        // Audio
        thread = new Thread(new Runnable() {
            @Override
            public void run() {
                int cur_off = 0;
                int len = DataManager.getInstance().audio_data.length;

                int off = 0;
                int step = DataManager.getInstance().bytes_per_video_frame * 15;

                while (true) {
                    if (isJumped) {
//                        System.out.println("start initAudio!!!!");
                        DataManager.getInstance().initAudio();
                        DataManager.getInstance().audio_video_offset = jump_event.targetFrame - 1;
                        cur_off = (jump_event.targetFrame - 1) * (DataManager.getInstance().bytes_per_video_frame / 4);
                        off = 0;
                        isJumped = false;
                    }

                    if (isStopped) {
                        DataManager.getInstance().audio_play_line.close();
                        DataManager.getInstance().audio_video_offset = 0;
                        cur_off = 0;
                        off = 0;
                        isStopped = false;
                    }

                    if (isPause) {
                        DataManager.getInstance().audio_play_line.stop();
                    } else {
                        if (!DataManager.getInstance().audio_play_line.isOpen()) {
                            try {
                                DataManager.getInstance().audio_play_line.open(DataManager.getInstance().audioFormat);
                            } catch (Exception e) {
                                System.out.println("Exception thrown:" + e);
                            }
                        }
                        DataManager.getInstance().audio_play_line.start();
                        if (cur_off + off < len) {
                            DataManager.getInstance().audio_play_line.write(DataManager.getInstance().audio_data, cur_off + off, step);
                            DataManager.getInstance().audio_play_line.drain();
                            off += step;
                        }
                    }
                }
            }
        });
    }

    public void setView(PrimaryVideoDisplayView view) {
        mView = view;
    }

    @Subscribe
    public void init_load(PrimarySlideEvent event) {
        String filename = DataManager.getInstance().getFilenameByFrameNo(event.newValue);
        DataManager.getInstance().currFrame = event.newValue;
//        if (futureTask)
        this.mView.showRGB(filename);
    }

    @Subscribe
    public void soundOnStart(StartEvent event) {
        if (!isInitiated) {
            thread.start();
//            futureTask.run();
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
        System.out.println("SET TRUE");
//        futureTask.cancel(true);

    }

    public void onMouseClicked() {

    }
}
