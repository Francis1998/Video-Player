package main.java.view.display;

import main.java.constants.DimensionConstants;
import main.java.presenter.display.PrimaryVideoDisplayPresenter;
import main.java.view.base.BaseViewGroup;

import java.awt.*;
import java.awt.event.MouseEvent;

public class PrimaryVideoDisplayView extends BaseViewGroup {
    PrimaryVideoDisplayPresenter mPresenter = null;
    VideoDisplayView mVideoDisplayView = null;

    public PrimaryVideoDisplayView() {
        super();
        this.setBackground(Color.lightGray);
        mPresenter = new PrimaryVideoDisplayPresenter(this);
        mVideoDisplayView = new VideoDisplayView(1);
//        mVideoDisplayView.setBackground(Color.GREEN);
        mVideoDisplayView.setPreferredSize(new Dimension(DimensionConstants.IMG_WIDTH, DimensionConstants.IMG_HEIGHT));
        this.add(mVideoDisplayView, BorderLayout.CENTER);
    }

    public PrimaryVideoDisplayView(PrimaryVideoDisplayPresenter presenter) {
        this();
        mPresenter = presenter;
        mPresenter.setView(this);
    }

    public void showRGB(String filename) {
        mVideoDisplayView.showRGB(filename);
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        mPresenter.onMouseClicked();
    }
}
