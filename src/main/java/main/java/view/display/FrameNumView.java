package main.java.view.display;

import main.java.data.DataManager;
import main.java.presenter.display.FrameNumPresenter;

import javax.swing.*;
import java.awt.*;

public class FrameNumView extends JPanel {
    FrameNumPresenter mPresenter = null;
    JTextField txtField;

    public FrameNumView() {
        super();
        mPresenter = new FrameNumPresenter(this);
        this.setPreferredSize(new Dimension(82, 40));
        this.setBackground(Color.lightGray);
        txtField = new JTextField();
        txtField.setText(String.valueOf(DataManager.getInstance().currFrame));
        txtField.setPreferredSize(new Dimension(80, 38));
        txtField.setEditable(false);
        this.add(txtField, BorderLayout.CENTER);
    }

    public void updateNum(Integer currFrame) {
        txtField.setText(String.valueOf(currFrame));
    }
}
