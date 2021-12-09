package main.java.data;

public class Link {
    public long ID = 0;
    public int sourceFrame;
    public String sourceFilePathBase;
    public int targetFrame;
    public String targetFilePathBase;
    public int duration = 10;// unit is frame
    public BoundingBox box;

    public Link(long ID,
                int sourceFrame,
                String sourceFilePathBase,
                int targetFrame,
                String targetFilePathBase,
                BoundingBox box) {
        this.ID = ID;
        this.sourceFrame = sourceFrame;
        this.sourceFilePathBase = sourceFilePathBase;
        this.targetFrame = targetFrame;
        this.targetFilePathBase = targetFilePathBase;
        this.box = box;
    }

    public String getSourceFilePathBase(){
        return sourceFilePathBase;
    }

}
