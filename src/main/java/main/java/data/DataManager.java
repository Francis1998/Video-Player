package main.java.data;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import main.java.constants.LiteralConstants;
import org.apache.commons.io.FileUtils;
import javax.sound.sampled.*;
import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class DataManager implements IDataManager {
    private static DataManager instance;

    private DataManager() {}

    public static DataManager getInstance() {
        if (instance == null) {
            instance = new DataManager();
        }
        return instance;
    }

    private Gson gson = new GsonBuilder().setPrettyPrinting().create();
    private String primaryVideoPathBase = "";
    private String suffixExtension = ".rgb";
    private int suffixLength = 8;

    // for audio
    private String audioExtension = ".wav";
    public int bytes_per_video_frame = 4 * 44100 / 30;

    // initialize when loading/changing videos
    public AudioInputStream audio_stream;
    public byte[] audio_data = new byte[44100 * 4 * 60 * 5];
    public AudioFormat audioFormat;
    public SourceDataLine audio_play_line;
    public int audio_freq_slow = 0;
    public int audio_video_offset = 0;
    public List<Link> LinkData = null;
    public HashMap<String, List<Link>> frameLinkMap = new HashMap<>();
    public int currFrame = 1;

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

    public String getPrimaryVideoPathBase() {
        return primaryVideoPathBase;
    }

    public String getPathFromJson(String filename, String frame) {
        String path = LinkData.get(0).getSourceFilePathBase();
        return path + path.substring(path.lastIndexOf("/")) + frame + ".rgb";
    }

    @Override
    public void getLinkListByFile(String filename) {
        try {
            Gson gson = new Gson();
            File file = new File(filename);
            String content = FileUtils.readFileToString(file);
            Link[] tempLinkList = gson.fromJson(content, Link[].class); // contains the whole reviews list
            LinkData = Arrays.asList(tempLinkList);
            setPrimaryVideo(DataManager.getInstance().LinkData.get(0).sourceFilePathBase);
        } catch (FileNotFoundException fileNotFoundException) {
            System.out.println(LiteralConstants.fileNotFoundException);
        } catch (IOException exception) {
            exception.printStackTrace();
        }
        listLinkToMap();
        LinkData = new ArrayList<>();
    }

    @Override
    public String getFilenameByFrameNo(int frame) {
        String frameString = Integer.toString(frame);
        StringBuilder sb = new StringBuilder();
        sb.append(primaryVideoPathBase);
        for (int i = 0; i < 4 - frameString.length(); i++) sb.append("0");
        sb.append(frameString);
        sb.append(suffixExtension);
        return sb.toString();
    }

    private String getBaseFilePath(String filename) {
        if (filename.length() <= suffixLength) return null;
        return filename.substring(0, filename.length() - suffixLength);
    }

    public void listLinkToMap() {
        for (Link link : LinkData) {
            if (link.targetFrame > 0 && link.sourceFrame > 0) {
                for (int i = link.sourceFrame; i <= link.sourceFrame + link.duration; i++) {
                    String pathFrameKey = link.sourceFilePathBase + i;
                    if (frameLinkMap.containsKey(pathFrameKey)) {
                        frameLinkMap.get(pathFrameKey).add(link);
                    } else {
                        frameLinkMap.put(pathFrameKey, new ArrayList<>());
                        frameLinkMap.get(pathFrameKey).add(link);
                    }
                }
            }
        }
    }

    public void initAudio() {
        audio_video_offset = 0;
        String audio_path = primaryVideoPathBase + audioExtension;
        try {
            // get audio stream
            audio_stream = AudioSystem.getAudioInputStream(new File(audio_path));
            // read audio stream
            audio_stream.read(audio_data, 0, audio_data.length);
            // update source data line
            AudioFormat formatIn = audio_stream.getFormat();
            audioFormat = new AudioFormat(formatIn.getSampleRate() - audio_freq_slow, formatIn.getSampleSizeInBits(), formatIn.getChannels(), true, formatIn.isBigEndian());
            DataLine.Info info = new DataLine.Info(SourceDataLine.class, audioFormat);
            audio_play_line = (SourceDataLine) AudioSystem.getLine(info);
            audio_play_line.open(audioFormat);
        } catch (Exception e) {
            System.out.println("Exception thrown:" + e);
        }
    }

    public int getDiffBetweenVideoAndAudio(){
        return currFrame - (audio_play_line.getFramePosition() / (bytes_per_video_frame / 4) + audio_video_offset);
    }
}
