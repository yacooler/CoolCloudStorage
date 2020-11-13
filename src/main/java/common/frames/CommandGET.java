package common.frames;


import common.fileobjects.FileParameters;
import io.netty.channel.ChannelHandlerContext;
import lombok.Getter;
import lombok.Setter;
import server.cloudusers.UserPool;
import server.database.entity.User;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class CommandGET extends BaseCommandFrame {

    @Setter @Getter
    private int currentFrame;

    @Setter @Getter
    private FileParameters fileParameters;

    public final int FRAME_SIZE = 16384;

    public CommandGET(int currentFrame) {
        this.currentFrame = currentFrame;
    }

    /**
     * Запрос с клиента на передачу данных с сервера на клиент
     * Наименование файла передается в качестве строкового параметра
     * Номер требуемого кадра в качестве следующего long
     * Получаем GET, в ответ отправляем кадр
     */
    @Override
    public BaseFrame serverProcessing(ChannelHandlerContext ctx, UserPool userPool, User user) {
        //System.out.println("GET");
        try {
            return sendFile(ctx);
        } catch (IOException e) {
            throw new RuntimeException("Не удалось отправить файл GET", e);
        }
    }

    private DataFrameTRN sendFile(ChannelHandlerContext ctx) throws IOException{
        //todo не открывать инпутстрим и не перематывать курсор каждый раз, сделать для каждого пользователя свои стримы

        //Объект для трансфера
        DataFrameTRN postContent = new DataFrameTRN();
        postContent.setLastFrame((int) fileParameters.getSize() / FRAME_SIZE);

        var reader = Files.newInputStream(Path.of(fileParameters.getName()));

        postContent.setContent(new byte[FRAME_SIZE]);
        postContent.setFileParameters(fileParameters);
        postContent.setDataOffset(currentFrame * FRAME_SIZE);
        postContent.currentFrame = currentFrame;

        int readed = 0;
        reader.skip(currentFrame * FRAME_SIZE);
        //читаем содержимое файла в ответный кадр
        readed = reader.read(postContent.getContent(), 0, FRAME_SIZE);

        postContent.contentDataSize = readed;

        reader.close();

        //Возвращаем кадр
        return postContent;
    }

}
