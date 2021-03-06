package common.utils;

import common.fileobjects.FileList;
import common.frames.CommandDIR;

import java.io.*;

public class SerializableFileUtils {

    public static FileList unserializeFileList(byte bytes[]){
        FileList result;

        try(
                ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(bytes);
                ObjectInputStream in = new ObjectInputStream(byteArrayInputStream)
                )
        {
            result = (FileList) in.readObject();
        } catch (IOException | ClassNotFoundException e) {
            throw new RuntimeException("Не удалось десеарилизовать FileList", e);
        }

        return result;

    }


}
