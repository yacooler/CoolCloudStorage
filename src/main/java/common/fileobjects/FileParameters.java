package common.fileobjects;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;

@ToString
public class FileParameters implements Serializable {

    private static final long serialVersionUID = -1878952550081914747L;

    public FileParameters(boolean isDirectory, String name, long size) {
        this.isDirectory = isDirectory;
        this.name = name;
        this.size = size;
    }

    @Getter @Setter
    private boolean isDirectory;

    @Getter @Setter
    private String name;

    @Getter @Setter
    private long size;

}
