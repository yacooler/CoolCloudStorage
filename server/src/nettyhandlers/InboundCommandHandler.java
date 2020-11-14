package nettyhandlers;


import cloudusers.UserPool;
import database.entity.User;
import frames.BaseFrame;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;


public class InboundCommandHandler extends ChannelInboundHandlerAdapter {

    private UserPool userPool;


    public InboundCommandHandler(UserPool userPool) {
        this.userPool = userPool;
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        userPool.addUser(ctx.channel(), new User());
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        userPool.removeUser(ctx.channel());
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        //System.out.println("InboundCommandHandler");

        //Получили пользователя, с которым работаем
        User user = userPool.getUser(ctx.channel());

        if (msg instanceof BaseFrame) {

            BaseFrame response;

            BaseFrame frame = (BaseFrame) msg;
            response = frame.serverProcessing(ctx, userPool, user);
            if (response!= null) {
                ctx.writeAndFlush(response);
            };

        }

    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        throw new RuntimeException("Ошибка Command Handler", cause);
    }
}
