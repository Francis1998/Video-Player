package main.java.presenter.display;

import com.google.common.eventbus.Subscribe;
import main.java.data.DataManager;
import main.java.event.*;
import main.java.presenter.base.BasePresenter;
import main.java.view.display.PrimaryVideoDisplayView;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.UnsupportedAudioFileException;
import java.io.IOException;

public class PrimaryVideoDisplayPresenter extends BasePresenter {
    PrimaryVideoDisplayView mView;
    long startTime;
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

        // Audio
        thread = new Thread(new Runnable() {
            @Override
            public void run() {
                int cur_off = 0;
                int len = DataManager.getInstance().audio_data.length;

                int off = 0;
                int step = DataManager.getInstance().bytes_per_video_frame*15;

                while (true) {
                    if (isJumped) {
                        DataManager.getInstance().initAudio();
                        DataManager.getInstance().audio_video_offset = (jump_event.targetFrame-1) * (44100/30);
                        cur_off = (jump_event.targetFrame-1) * (DataManager.getInstance().bytes_per_video_frame / 4);
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
//        if (event.newValue == 1){
//            startTime=System.currentTimeMillis();
//        } else if (event.newValue == 30){
//            System.out.println(System.currentTimeMillis() - startTime);
//        }
        this.mView.showRGB(filename);
//        System.out.println("this is show rgb");
//        String filename = ;
//        showRGB(filename);
    }

    @Subscribe
    public void soundOnStart(StartEvent event) {
        if (!isInitiated){
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
