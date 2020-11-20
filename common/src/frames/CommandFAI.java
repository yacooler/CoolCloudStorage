package frames;

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
    public BaseFrame processing(ChannelHandlerContext ctx){
        return null;
    }
}
