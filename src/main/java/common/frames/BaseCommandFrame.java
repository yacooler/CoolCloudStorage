package common.frames;

public abstract class BaseCommandFrame extends BaseFrame {
    public BaseCommandFrame() {}

    public BaseCommandFrame(byte[] content) {
        super(content);
    }

    public BaseCommandFrame(String content) {
        super(content);
    }
}
