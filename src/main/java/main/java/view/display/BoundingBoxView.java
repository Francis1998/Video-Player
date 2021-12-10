package main.java.view.display;

import main.java.data.BoundingBox;
import main.java.data.Link;
import main.java.view.base.BaseViewGroup;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;

public class BoundingBoxView extends JPanel {
    Link link = null;
    public BoundingBoxView(Link link) {
        super();
        this.link = link;
        BoundingBox box = link.box;
        this.setBounds(box.x, box.y, box.width, box.height);
        this.setOpaque(false);
        this.setBorder(BorderFactory.createLineBorder(Color.BLUE));
    }

    public long getID() {
        return link.ID;
    }

}
