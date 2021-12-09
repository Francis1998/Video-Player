package main.java.data;

import java.io.FileNotFoundException;
import java.util.List;

public interface IDataManager {
    String parseXML(String filename);

    void saveLinks(List<Link> links);

    void save();

    List<Link> getLinkListByFrameNo(int frame);

    void addLinkAtFrameNo(Link link);

    void setPrimaryVideo(String filename);

    String getFilenameByFrameNo(int frame);

    void getLinkListByFile(String filename) throws FileNotFoundException;

}
