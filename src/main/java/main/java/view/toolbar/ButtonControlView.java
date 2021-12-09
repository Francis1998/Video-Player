package main.java.view.toolbar;

import javax.swing.*;
import java.awt.*;

public class ButtonControlView extends JPanel {
    ActionView actionView = null;

    public ButtonControlView() {
        super();
        createUI();
        this.setBackground(Color.lightGray);
    }

    public void createUI() {
        actionView = new ActionView();
        this.add(actionView);
    }
}
