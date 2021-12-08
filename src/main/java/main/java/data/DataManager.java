package main.java.data;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.stream.JsonReader;
import main.java.constants.Constants;

import org.apache.commons.io.FileUtils;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.UnsupportedAudioFileException;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;



public class DataManager implements IDataManager {
    private static DataManager instance;
    private DataManager(){}
    private Gson gson = new GsonBuilder().setPrettyPrinting().create();
    public static DataManager getInstance() {
        if (instance == null) {
            instance = new DataManager();
        }
        return instance;
    }


    private String primaryVideoPathBase = "";
//    private String secondaryVideoPathBase = "";
    private String suffixExtension = ".rgb";
    private int suffixLength = 8;

    // for audio
    private String audioExtension = ".wav";

    public List<Link> LinkData = null;
    public HashMap<String, List<Link>> frameLinkMap = new HashMap<>();
    public int currFrame = 1;
//    public int seq = 1;
    @Override
    public String parseXML(String filename) {

        return null;
    }

    @Override
    public void saveLinks(List<Link> links) {
        String jsonString = gson.toJson(links);
    }

    @Override
    public void save() {

    }

    @Override
    public List<Link> getLinkListByFrameNo(int frame) {
        return null;
    }

    @Override
    public void addLinkAtFrameNo(Link link) {

    }

    @Override
    public void setPrimaryVideo(String filename) {
        primaryVideoPathBase = filename;
    }

    public String getPrimaryVideoPathBase(){
        return primaryVideoPathBase;
    }

    public String getPathFromJson(String filename, String frame){
        String path = LinkData.get(0).getSourceFilePathBase();
        return path + path.substring(path.lastIndexOf("/")) + frame + ".rgb";
    }
    @Override
    public List<Link> getLinkListByFile(String filename) {
        try {
            Gson gson = new Gson();
            File file = new File(filename);
            String content = FileUtils.readFileToString(file);
            Link[] tempLinkList = gson.fromJson(content, Link[].class); // contains the whole reviews list
            LinkData = Arrays.asList(tempLinkList);
//            for (Link l: LinkData){
//                l.toScreen();
//            }
//            for (Link l : LinkData) {
//                l.toScreen();
//            }
        } catch (FileNotFoundException fileNotFoundException) {
            fileNotFoundException.printStackTrace();
        } catch (IOException exception) {
            exception.printStackTrace();
        }
        listLinkToMap();
        return LinkData;
    }

    @Override
    public String getFilenameByFrameNo(int frame) {
        String frameString = Integer.toString(frame);
        StringBuilder sb = new StringBuilder();
        sb.append(primaryVideoPathBase);
        for (int i = 0; i < 4 - frameString.length(); i++) sb.append("0");
        sb.append(frameString);
        sb.append(suffixExtension);
//        System.out.println("path: " + sb.toString());
        return sb.toString();
    }

    private String getBaseFilePath(String filename) {
        if (filename.length() <= suffixLength) return null;
        return filename.substring(0, filename.length() - suffixLength);
    }

    public void listLinkToMap(){
        for(Link link:LinkData){
            for (int i = link.sourceFrame; i <= link.sourceFrame + link.duration; i ++){
                String pathFrameKey = link.sourceFilePathBase + i;
//                System.out.println("pathkey: " + pathFrameKey);
                if (frameLinkMap.containsKey(pathFrameKey)){
                    frameLinkMap.get(pathFrameKey).add(link);
                } else {
                    frameLinkMap.put(pathFrameKey, new ArrayList<>());
                    frameLinkMap.get(pathFrameKey).add(link);
                }
            }
        }
    }

    public AudioInputStream getSound(int frame_no) throws UnsupportedAudioFileException, IOException {
        String sound_file = primaryVideoPathBase + audioExtension;

        AudioInputStream sound = AudioSystem.getAudioInputStream(new File(sound_file));
        AudioFormat format = sound.getFormat();

        long bytes_to_skip = (int) format.getFrameSize() * ((int)format.getFrameRate()) * (frame_no-1) / 30;

        long justSkipped = 0;
        while (bytes_to_skip > 0 && (justSkipped = sound.skip(bytes_to_skip)) > 0) {
            bytes_to_skip -= justSkipped;
        }

        return sound;
    }
}
