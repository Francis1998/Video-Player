package main.java.util;

import main.java.constants.DimensionConstants;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;

public class ImgUtil {

    public static void readImageRGB(String imgPath, BufferedImage img) {
        try {
            int frameLength = DimensionConstants.IMG_WIDTH * DimensionConstants.IMG_HEIGHT * 3;

            File file = new File(imgPath);
            RandomAccessFile raf = new RandomAccessFile(file, "r");
            raf.seek(0);

            long len = frameLength;
            byte[] bytes = new byte[(int) len];

            raf.read(bytes);

            int ind = 0;
            for (int y = 0; y < DimensionConstants.IMG_HEIGHT; y++) {
                for (int x = 0; x < DimensionConstants.IMG_WIDTH; x++) {
                    byte a = 0;
                    byte r = bytes[ind];
                    byte g = bytes[ind + DimensionConstants.IMG_HEIGHT * DimensionConstants.IMG_WIDTH];
                    byte b = bytes[ind + DimensionConstants.IMG_HEIGHT * DimensionConstants.IMG_WIDTH * 2];

                    int pix = 0xff000000 | ((r & 0xff) << 16) | ((g & 0xff) << 8) | (b & 0xff);
                    //int pix = ((a << 24) + (r << 16) + (g << 8) + b);
                    img.setRGB(x, y, pix);
                    ind++;
                }
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
