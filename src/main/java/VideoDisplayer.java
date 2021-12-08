import main.java.view.display.VideoAuthoringView;

import javax.swing.*;
import java.awt.*;

public class VideoDisplayer {
    public static void main(String[] args){
        JFrame frame = new JFrame("project");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        var pane = frame.getContentPane();
        var video = new VideoAuthoringView();
        video.setPreferredSize(new Dimension(530, 410));
        pane.add(video);
        frame.pack();
        frame.setVisible(true);
        frame.setTitle("Hypervideo Player");
    }

}
