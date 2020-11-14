package frames;

import cloudusers.UserPool;
import database.entity.User;
import io.netty.channel.ChannelHandlerContext;

public class CommandFAI extends BaseCommandFrame {
    public CommandFAI() {
        super();
    }

    public CommandFAI(byte[] content) {
        super(content);
    }

    public CommandFAI(String content) {
        super(content);
    }

    @Override
    protected void afterConstruct() {
        semantic = "FAI".getBytes();
    }


    @Override
    public BaseFrame serverProcessing(ChannelHandlerContext ctx, UserPool userPool, User user){
        return null;
    }
}
