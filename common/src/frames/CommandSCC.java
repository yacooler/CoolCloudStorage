package frames;


import cloudusers.UserPool;
import database.entity.User;
import io.netty.channel.ChannelHandlerContext;

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
