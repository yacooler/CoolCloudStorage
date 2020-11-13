package common.frames;

import common.fileobjects.FileList;
import common.fileobjects.FileParameters;
import io.netty.channel.ChannelHandlerContext;
import lombok.Getter;
import lombok.Setter;
import server.cloudusers.UserPool;
import server.database.entity.User;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.stream.Collectors;

public class CommandDIR extends BaseCommandFrame {

    public CommandDIR() {}

    public CommandDIR(byte[] content) {
        super(content);
    }

    public CommandDIR(String content) {
        super(content);
    }

    @Getter @Setter
    private FileList fileList = new FileList();


    /**
     * Получение данных о директории, путь к которой относительно root - папки запросил пользователь
     * В ответ на запрос возвращаем пришедший объект с заполненным fileList
     */
    @Override
    public BaseFrame serverProcessing(ChannelHandlerContext ctx, UserPool userPool, User user) {
        StringBuilder builder = new StringBuilder();

        try {
            builder = builder.append("storage/").append(user.getName());
            if (!getContentAsString().isEmpty()) builder.append(getContentAsString());
            var files = Files.list(Path.of(builder.toString())).collect(Collectors.toList());

            fileList.getFiles().clear();

            for (Path path: files) {
                fileList.add(new FileParameters(
                        Files.isDirectory(path),
                        path.toString(),
                        Files.size(path)
                ));
            }

        } catch (IOException e) {
            throw new RuntimeException("Не удалось прочитать список файлов из директории " + builder.toString(), e);
        }

        return this;

    }
}
