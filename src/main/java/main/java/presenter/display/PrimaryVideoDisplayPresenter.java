package main.java.presenter.display;

import com.google.common.eventbus.Subscribe;
import main.java.data.DataManager;
import main.java.event.PrimarySlideEvent;
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
//        System.out.println("this is show rgb");
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

    @Subscribe
    public void play_sound(PrimarySlideEvent event) {
        // this.mView.playSound(event.newValue);
        DataManager.getInstance().playSound(event.newValue);
    }

    public void onMouseClicked() {

    }
}
