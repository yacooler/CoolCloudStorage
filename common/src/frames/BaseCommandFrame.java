package frames;

public abstract class BaseCommandFrame extends BaseFrame {
    public BaseCommandFrame() {}

    public BaseCommandFrame(byte[] content) {
        super(content);
    }

    public BaseCommandFrame(String content) {
        super(content);
    }

    @Override
    public String getContentAsString() {
        return super.getContentAsString();
    }
}
