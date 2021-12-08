package main.java.view.display;

import main.java.constants.DimensionConstants;
import main.java.data.DataManager;
import main.java.data.Link;
import main.java.event.PrimarySlideEvent;
import main.java.eventbus.EventBusCenter;
import main.java.presenter.display.VideoDisplayPresenter;
import main.java.util.ImgUtil;
import main.java.view.base.BaseViewGroup;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.awt.image.DataBuffer;
import java.io.IOException;

import javax.sound.sampled.*;

public class VideoDisplayView extends BaseViewGroup {
    VideoDisplayPresenter mPresenter;
    BufferedImage imgOne;
    ImageIcon imageIconOne;

    Integer curFrame = 1;

    int AUDIO_BUFFER = 4*44100/30; // one second

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

        this.repaint();
    }

    public void playSound(AudioInputStream audioStream) throws LineUnavailableException, IOException {
        AudioFormat audioFormat = audioStream.getFormat();
        DataLine.Info info = new DataLine.Info(SourceDataLine.class, audioFormat);
        SourceDataLine audioOutput = (SourceDataLine) AudioSystem.getLine(info);
        audioOutput.open(audioFormat);
        audioOutput.start();

        int nBytesRead = 0;

        byte[] abData = new byte[AUDIO_BUFFER];

        nBytesRead = audioStream.read(abData, 0, abData.length);
        if (nBytesRead >= 0) {
            audioOutput.write(abData, 0, nBytesRead);
        }

        audioOutput.drain();
        audioOutput.close();
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        super.mouseClicked(e);
        System.out.println("click here");
        System.out.println(DataManager.getInstance().getPrimaryVideoPathBase() + " " +DataManager.getInstance().currFrame);
        String curKey = DataManager.getInstance().getPrimaryVideoPathBase() + DataManager.getInstance().currFrame;
//        System.out.println(curKey);
//        for (String s:DataManager.getInstance().frameLinkMap.keySet()){
//            System.out.println("dictkey" + s);
//        }
        if (DataManager.getInstance().frameLinkMap.containsKey(curKey)){

            for (Link l: DataManager.getInstance().frameLinkMap.get(curKey)){
                if (e.getX() >= l.box.x && e.getX() <= l.box.x + l.box.width && e.getY() >= l.box.y && e.getY() <= l.box.y + l.box.height){

                    DataManager.getInstance().setPrimaryVideo(l.targetFilePathBase);
                    EventBusCenter.post(new PrimarySlideEvent(l.targetFrame));
                    DataManager.getInstance().currFrame = l.targetFrame;
                }
            }
        }
    }
}
