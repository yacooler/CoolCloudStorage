package common.frames;


import io.netty.channel.ChannelHandlerContext;
import server.cloudusers.UserPool;
import server.database.entity.User;

/**
 * Success. Команда отправляется в случае, если предыдущая операция завершена успешно
 */
public class CommandSCC extends BaseCommandFrame {
    public CommandSCC() {
        super();
    }

    public CommandSCC(byte[] content) {
        super(content);
    }

    public CommandSCC(String content) {
        super(content);
    }

    @Override
    protected void afterConstruct() {
        semantic = "SCC".getBytes();
    }

    @Override
    public BaseFrame serverProcessing(ChannelHandlerContext ctx, UserPool userPool, User user){
        return null;
    }
}
