package main.java.view.display;

import com.google.common.eventbus.Subscribe;
import main.java.data.DataManager;
import main.java.event.PrimarySlideEvent;
import main.java.presenter.display.PrimaryVideoDisplayPresenter;
import main.java.view.base.BaseViewGroup;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.LineUnavailableException;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.io.IOException;

public class PrimaryVideoDisplayView extends BaseViewGroup {
    PrimaryVideoDisplayPresenter mPresenter = null;
    VideoDisplayView mVideoDisplayView = null;

    public PrimaryVideoDisplayView() {
        super();
        this.setBackground(Color.BLUE);
//        this.setPreferredSize(new Dimension(450, 400));
        mPresenter = new PrimaryVideoDisplayPresenter(this);
        mVideoDisplayView = new VideoDisplayView(1);
        mVideoDisplayView.setBackground(Color.BLACK);
        mVideoDisplayView.setPreferredSize(new Dimension(400, 300));
        this.add(mVideoDisplayView, BorderLayout.CENTER);
    }

    public PrimaryVideoDisplayView(PrimaryVideoDisplayPresenter presenter) {
        this();
        mPresenter = presenter;
        mPresenter.setView(this);
    }

    public void showRGB(String filename) {
//        System.out.println(filename);
        mVideoDisplayView.showRGB(filename);
    }

//    public void playSound(int frame_no){
//        mVideoDisplayView.playSound(frame_no);
//    }

    @Override
    public void mouseClicked(MouseEvent e) {
        mPresenter.onMouseClicked();
    }
}
