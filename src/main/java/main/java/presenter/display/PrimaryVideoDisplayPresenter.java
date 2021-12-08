package main.java.presenter.display;

import com.google.common.eventbus.Subscribe;
import main.java.data.DataManager;
import main.java.event.JumpEvent;
import main.java.event.PauseEvent;
import main.java.event.PrimarySlideEvent;
import main.java.event.StartEvent;
import main.java.presenter.base.BasePresenter;
import main.java.view.display.PrimaryVideoDisplayView;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.UnsupportedAudioFileException;
import java.io.IOException;

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
        System.out.println("this is show rgb");
//        String filename = ;
//        showRGB(filename);
    }

//    @Subscribe
//    public void play_sound(PrimarySlideEvent event) {
//        // this.mView.playSound(event.newValue);
//        DataManager.getInstance().playSound(event.newValue);
//    }

    @Subscribe
    public void soundOnStart(StartEvent event) {
        // System.out.println("Video frame:" + DataManager.getInstance().currFrame);
        // System.out.println("Sound frame:" + DataManager.getInstance().audio_play_line.getFramePosition());
        DataManager.getInstance().is_audio_playing = true;
        int cur_off = DataManager.getInstance().audio_cur_frame * DataManager.getInstance().bytes_per_video_frame;
        int len = DataManager.getInstance().audio_data.length;
        DataManager.getInstance().audio_play_line.start();
        int off = 0;
        int step = DataManager.getInstance().bytes_per_video_frame * 30;
        while (DataManager.getInstance().is_audio_playing) {
            if (cur_off + off < len) {
                DataManager.getInstance().audio_play_line.write(DataManager.getInstance().audio_data, cur_off + off, step);
                DataManager.getInstance().audio_play_line.drain();
                off += step;
            }
        }
    }

    @Subscribe
    public void soundOnPause(PauseEvent event) {
        DataManager.getInstance().is_audio_playing = false;
        DataManager.getInstance().audio_play_line.stop();
        DataManager.getInstance().audio_cur_frame = DataManager.getInstance().audio_play_line.getFramePosition();
    }

    @Subscribe
    public void soundOnJump(JumpEvent event) {
        boolean is_playing = DataManager.getInstance().is_audio_playing;
        if (is_playing) {
            DataManager.getInstance().is_audio_playing = false;
        }
        DataManager.getInstance().initAudio();
        DataManager.getInstance().audio_cur_frame = event.targetFrame * (DataManager.getInstance().bytes_per_video_frame / 4);
        if (is_playing) {
            DataManager.getInstance().is_audio_playing = true;
        }
    }

    public void onMouseClicked() {

    }
}
