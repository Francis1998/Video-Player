package main.java.view.display;

import com.google.common.eventbus.Subscribe;
import main.java.constants.DimensionConstants;
import main.java.constants.LiteralConstants;
import main.java.data.DataManager;
import main.java.data.Link;
import main.java.event.JumpEvent;
import main.java.event.PrimarySlideEvent;
import main.java.event.ShowEvent;
import main.java.eventbus.EventBusCenter;
import main.java.presenter.display.VideoDisplayPresenter;
import main.java.util.ImgUtil;
import main.java.util.StringUtil;
import main.java.view.base.BaseViewGroup;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

public class VideoDisplayView extends BaseViewGroup {
    VideoDisplayPresenter mPresenter;
    BufferedImage imgOne;
    ImageIcon imageIconOne;
    Integer curFrame = 1;
    JLabel imgLabel;
    List<BoundingBoxView> boxViews = new ArrayList<>();
    JLayeredPane layeredPane = new JLayeredPane();
    boolean IsShow = false;
    public VideoDisplayView(Integer curFrame) {
        super();
        this.curFrame = curFrame;
        createUI();
    }

    public void createUI() {
        this.setLayout(null);
        this.setPreferredSize(new Dimension(DimensionConstants.IMG_WIDTH, DimensionConstants.IMG_HEIGHT));
        mPresenter = new VideoDisplayPresenter(this);
        imgOne = new BufferedImage(DimensionConstants.IMG_WIDTH, DimensionConstants.IMG_HEIGHT, BufferedImage.TYPE_INT_RGB);
        imageIconOne = new ImageIcon(imgOne);
        imgLabel = new JLabel(imageIconOne);
        imgLabel.setBackground(Color.ORANGE);
        imgLabel.setBounds(0, 0, DimensionConstants.IMG_WIDTH, DimensionConstants.IMG_HEIGHT);
        imgLabel.addMouseListener(this); //Todo: delete or not?
        layeredPane.add(imgLabel, 0);
        layeredPane.setBounds(0, 0, DimensionConstants.IMG_WIDTH, DimensionConstants.IMG_HEIGHT);
        layeredPane.setLayer(imgLabel, 0);
        add(layeredPane);
        this.setPreferredSize(new Dimension(DimensionConstants.IMG_WIDTH, DimensionConstants.IMG_HEIGHT));
    }

    public void showRGB(String filename) {
        ImgUtil.readImageRGB(filename, imgOne);
        layeredPane.revalidate();
        layeredPane.repaint();
    }

    public BoundingBoxView createBoxView(Link link) {
        BoundingBoxView view = new BoundingBoxView(link);
        return view;
    }

    private void updateSelectedFrame() {
        for (BoundingBoxView view : this.boxViews) {
            view.setBorder(BorderFactory.createLineBorder(Color.BLUE));
        }
    }

    public void showBoundingbox(ShowEvent event){
        System.out.println("show click");
        if (!IsShow){
            IsShow = true;
        }
        else{
            IsShow = false;
        }
        refreshBoundingBox(DataManager.getInstance().currFrame);
    }

    public void continueRefreshBoundingBox(PrimarySlideEvent event){
        refreshBoundingBox(event.newValue);
    }

    public void refreshBoundingBox(int currFrame) {
        if (!IsShow){
            for (JPanel view : boxViews) layeredPane.remove(view);
            boxViews.clear();
            layeredPane.invalidate();
            layeredPane.repaint();
        } else {
            for (JPanel view : boxViews) layeredPane.remove(view);
            boxViews.clear();
            List<Link> links = DataManager.getInstance().getLinkListByFrameNo(currFrame);
            if (links == null){
                layeredPane.invalidate();
                layeredPane.repaint();
                return;
            }
            for (Link link : links) {
                boxViews.add(createBoxView(link));
            }
            for (int i = 1; i <= boxViews.size(); i++) {
                layeredPane.add(boxViews.get(i - 1), i, i);
            }
            updateSelectedFrame();
            layeredPane.invalidate();
            layeredPane.repaint();
        }
//        layeredPane.invalidate();
//        layeredPane.repaint();
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        super.mouseClicked(e);
        String curKey = DataManager.getInstance().getPrimaryVideoPathBase() + DataManager.getInstance().currFrame;
        if (DataManager.getInstance().frameLinkMap.containsKey(curKey)) {
            for (Link l : DataManager.getInstance().frameLinkMap.get(curKey)) {
                if (DataManager.getInstance().currFrame == l.targetFrame &&
                        StringUtil.equal(DataManager.getInstance().getPrimaryVideoPathBase(), l.targetFilePathBase)) continue;
                if (e.getX() >= l.box.x && e.getX() <= l.box.x + l.box.width && e.getY() >= l.box.y && e.getY() <= l.box.y + l.box.height) {
                    System.out.println("log: " + l);
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
