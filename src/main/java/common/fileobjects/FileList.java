package common.fileobjects;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class FileList implements Serializable {

    private static final long serialVersionUID = -2534626971804290540L;

    private List<FileParameters> files = new ArrayList<>();

    public void add(FileParameters fileParameters){
        files.add(fileParameters);
    }

    public List<FileParameters> getFiles(){
        return files;
    }

    public Optional<FileParameters> getByFilename(String filename){
        FileParameters found = null;
        for (FileParameters fileParam: files) {
            if (fileParam.getName().equalsIgnoreCase(filename)) {
                found = fileParam;
                break;
            }
        }
        return Optional.ofNullable(found);
    }

}
