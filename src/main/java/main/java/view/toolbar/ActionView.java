package main.java.view.toolbar;

import main.java.constants.LiteralConstants;
import main.java.event.PauseEvent;
import main.java.event.StartEvent;
import main.java.event.StopEvent;
import main.java.eventbus.EventBusCenter;
import main.java.presenter.display.ActionPresenter;
import main.java.view.base.BaseViewGroup;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.io.File;

public class ActionView extends BaseViewGroup {
    JButton playButton = null;
    JButton pauseButton = null;
    JButton stopButton = null;
    JButton importPrimary = null;
    ActionPresenter mPresenter = null;

    public ActionView() {
        super();
        createUI();
        mPresenter = new ActionPresenter(this);

    }

    public void createUI() {
        Icon playIcon = scaleImage(new ImageIcon(LiteralConstants.playPicFilePath), 50, 50);
        Icon pauseIcon = scaleImage(new ImageIcon(LiteralConstants.pausePicFilePath), 50, 50);
        Icon stopIcon = scaleImage(new ImageIcon(LiteralConstants.stopPicFilePath), 50, 50);

        this.playButton = new JButton(playIcon);
        this.pauseButton = new JButton(pauseIcon);
        this.stopButton = new JButton(stopIcon);
        this.importPrimary = new JButton(LiteralConstants.importP);
        this.playButton.addMouseListener(this);
        this.pauseButton.addMouseListener(this);
        this.stopButton.addMouseListener(this);
        this.importPrimary.addMouseListener(this);

        this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        this.add(this.importPrimary, BorderLayout.CENTER);
        this.add(this.playButton, BorderLayout.CENTER);
        this.add(this.pauseButton, BorderLayout.CENTER);
        this.add(this.stopButton, BorderLayout.CENTER);
        this.setBackground(Color.lightGray);
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        if (e.getSource() == this.playButton) {
            EventBusCenter.post(new StartEvent());
            System.out.println("playButton");
        } else if (e.getSource() == this.pauseButton) {
            EventBusCenter.post(new PauseEvent());
            System.out.println("pauseButton");
        } else if (e.getSource() == this.stopButton) {
            EventBusCenter.post(new StopEvent());
            System.out.println("stopButton");
        } else if (e.getSource() == this.importPrimary) {
            onOpenFile();
        }
    }

    private void onOpenFile() {
        final JFileChooser fc = new JFileChooser();
        int returnVal = fc.showOpenDialog(this);
        if (returnVal == JFileChooser.APPROVE_OPTION) {
            File file = fc.getSelectedFile();
            mPresenter.onOpenFile(file);
        }
    }

    public ImageIcon scaleImage(ImageIcon icon, int w, int h) {
        int nw = icon.getIconWidth();
        int nh = icon.getIconHeight();

        if (icon.getIconWidth() > w) {
            nw = w;
            nh = (nw * icon.getIconHeight()) / icon.getIconWidth();
        }

        if (nh > h) {
            nh = h;
            nw = (icon.getIconWidth() * nh) / icon.getIconHeight();
        }

        return new ImageIcon(icon.getImage().getScaledInstance(nw, nh, Image.SCALE_DEFAULT));
    }

}
