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
        System.out.println("this is showrgb");
        String filename = DataManager.getInstance().getFilenameByFrameNo(event.newValue);
        this.mView.showRGB(filename);
//        String filename = ;
//        showRGB(filename);
    }

    @Subscribe
    public void play_sound(PrimarySlideEvent event) {
        try {
            AudioInputStream sound = DataManager.getInstance().getSound(event.newValue);
            this.mView.playSound(sound);
        } catch (UnsupportedAudioFileException e1) {
            System.out.println("Exception thrown  :" + e1);
        } catch (IOException e2) {
            System.out.println("Exception thrown  :" + e2);
        }
    }

    public void onMouseClicked() {

    }
}
