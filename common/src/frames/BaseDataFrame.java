package frames;


import fileobjects.FileParameters;
import lombok.Getter;
import lombok.Setter;

public class BaseDataFrame extends BaseFrame {

    @Getter @Setter
    protected int currentFrame = -1;

    @Getter @Setter
    private int lastFrame = -1;

    @Getter @Setter
    private int dataOffset = 0;

    @Getter @Setter
    private FileParameters fileParameters;

    @Getter @Setter
    private boolean initialized;

    public BaseDataFrame() {
    }

}
