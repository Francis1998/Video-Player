package main.java.view.display;

import main.java.view.toolbar.ButtonControlView;

import javax.swing.*;
import java.awt.*;

public class VideoAuthoringView extends JPanel {
    PrimaryVideoDisplayView mPrimaryVideoDisplayView;
    ButtonControlView mButtonControlView;
    FrameNumView mFrameNumView;

    public VideoAuthoringView() {
        super();
        this.setBackground(Color.lightGray);
        mButtonControlView = new ButtonControlView();
        mFrameNumView = new FrameNumView();
        mPrimaryVideoDisplayView = new PrimaryVideoDisplayView();
//        mPrimaryVideoDisplayView.setPreferredSize(new Dimension(400, 350));

        this.add(mPrimaryVideoDisplayView, BorderLayout.CENTER);
        this.add(mButtonControlView, BorderLayout.EAST);
        this.add(mFrameNumView, BorderLayout.EAST);
    }
}
