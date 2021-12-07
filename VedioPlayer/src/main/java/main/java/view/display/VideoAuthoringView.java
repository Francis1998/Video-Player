package main.java.view.display;

import main.java.view.toolbar.ButtonControlView;

import javax.swing.*;
import java.awt.*;

public class VideoAuthoringView extends JPanel {
    PrimaryVideoDisplayView mPrimaryVideoDisplayView;
    ButtonControlView mButtonControlView;

    public VideoAuthoringView() {
        super();
        this.setBackground(Color.GREEN);
        mButtonControlView = new ButtonControlView();
        mPrimaryVideoDisplayView = new PrimaryVideoDisplayView();
        mPrimaryVideoDisplayView.setPreferredSize(new Dimension(400, 350));

        this.add(mPrimaryVideoDisplayView, BorderLayout.CENTER);
        this.add(mButtonControlView, BorderLayout.EAST);
    }
}
