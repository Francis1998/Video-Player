package main.java.constants;

import com.google.common.reflect.TypeToken;
import main.java.data.Link;

import java.lang.reflect.Type;
import java.util.List;

public class Constants {
    public static final String first = "FIRST";
    public static final String second = "SECOND";
    public static final String importP = "Import List of Link";
    public static final String importS = "Import Secondary Video";
    public static final String createNew = "Create new hyperlink";
    public static final String play = "play";
    public static final String pause = "pause";
    public static final String stop = "stop";
    public static final Type REVIEW_TYPE = new TypeToken<List<Link>>() {}.getType();
}
