package main.java.event;

public class JumpEvent {
    public String targetFilePathBase = null;
    public Integer targetFrame = null;

    public JumpEvent(String targetFilePathBase, Integer targetFrame) {
        this.targetFilePathBase = targetFilePathBase;
        this.targetFrame = targetFrame;
    }
}
