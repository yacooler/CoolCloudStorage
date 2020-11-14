package frames;

import cloudusers.UserPool;
import database.entity.User;
import io.netty.channel.ChannelHandlerContext;

public class CommandNEW extends BaseCommandFrame {
    public CommandNEW() {
        super();
    }

    public CommandNEW(byte[] content) {
        super(content);
    }

    public CommandNEW(String content) {
        super(content);
    }

    @Override
    protected void afterConstruct() {
        semantic = "NEW".getBytes();
    }


    @Override
    public BaseFrame serverProcessing(ChannelHandlerContext ctx, UserPool userPool, User user){
        //Реализация команды NEW - клиент присылает NEW на сервер, сервер отвечает LOG - запрос логина
        System.out.println("new user connected and sent NEW");
        return new CommandLOG("server is waiting for login");
    }

}
