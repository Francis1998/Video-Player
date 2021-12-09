package main.java.view.display;

import main.java.constants.DimensionConstants;
import main.java.constants.LiteralConstants;
import main.java.data.DataManager;
import main.java.data.Link;
import main.java.event.JumpEvent;
import main.java.event.PrimarySlideEvent;
import main.java.eventbus.EventBusCenter;
import main.java.presenter.display.VideoDisplayPresenter;
import main.java.util.ImgUtil;
import main.java.util.StringUtil;
import main.java.view.base.BaseViewGroup;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;

public class VideoDisplayView extends BaseViewGroup {
    VideoDisplayPresenter mPresenter;
    BufferedImage imgOne;
    ImageIcon imageIconOne;

    Integer curFrame = 1;
    JLabel label;

    public VideoDisplayView(Integer curFrame) {
        super();
        this.curFrame = curFrame;
        createUI();
    }

    public void createUI() {
        this.setPreferredSize(new Dimension(DimensionConstants.IMG_WIDTH, DimensionConstants.IMG_HEIGHT));
        mPresenter = new VideoDisplayPresenter(this);
        imgOne = new BufferedImage(DimensionConstants.IMG_WIDTH, DimensionConstants.IMG_HEIGHT, BufferedImage.TYPE_INT_RGB);
        imageIconOne = new ImageIcon(imgOne);
        label = new JLabel(imageIconOne);
        label.addMouseListener(this);
        add(label, BorderLayout.CENTER);

    }

    public void showRGB(String filename) {
        ImgUtil.readImageRGB(filename, imgOne);
        this.revalidate();
        this.repaint();
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        super.mouseClicked(e);
        System.out.println("click here");
        String curKey = DataManager.getInstance().getPrimaryVideoPathBase() + DataManager.getInstance().currFrame;
        if (DataManager.getInstance().frameLinkMap.containsKey(curKey)) {
            for (Link l : DataManager.getInstance().frameLinkMap.get(curKey)) {
                if (DataManager.getInstance().currFrame == l.targetFrame &&
                        StringUtil.equal(DataManager.getInstance().getPrimaryVideoPathBase(), l.targetFilePathBase)) continue;
                if (e.getX() >= l.box.x && e.getX() <= l.box.x + l.box.width && e.getY() >= l.box.y && e.getY() <= l.box.y + l.box.height) {
                    DataManager.getInstance().setPrimaryVideo(l.targetFilePathBase);
                    DataManager.getInstance().getLinkListByFile(l.targetFilePathBase + LiteralConstants.suffixJson);
                    EventBusCenter.post(new PrimarySlideEvent(l.targetFrame));
                    EventBusCenter.post(new JumpEvent(l.targetFilePathBase, l.targetFrame));
                    DataManager.getInstance().currFrame = l.targetFrame;
                }
            }
        }
    }
}
