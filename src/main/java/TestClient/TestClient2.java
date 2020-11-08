package TestClient;

import commands.*;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.serialization.ClassResolvers;
import io.netty.handler.codec.serialization.ObjectDecoder;
import io.netty.handler.codec.serialization.ObjectEncoder;
import utils.AuthUtils;

import java.util.Scanner;

public class TestClient2 {
    private static String login;
    private static String password;

    public static void main(String[] args) throws Exception {
        EventLoopGroup group = new NioEventLoopGroup();
        try {
            new Bootstrap().group(group)
                    .channel(NioSocketChannel.class)
                    .handler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        public void initChannel(SocketChannel socketChannel) {
                            ChannelPipeline pipeline = socketChannel.pipeline();
                            pipeline.addLast(new ObjectEncoder());
                            pipeline.addLast(new ObjectDecoder(ClassResolvers.cacheDisabled(null)));
                            pipeline.addLast(new ChannelInboundHandlerAdapter() {
                                @Override
                                public void channelActive(ChannelHandlerContext ctx) throws Exception {
                                    //Новый пользователь
                                    ctx.writeAndFlush(new CommandNEW());
                                }

                                @Override
                                public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
                                    BaseCommand command = (BaseCommand) msg;
                                    BaseCommand response;
                                    Scanner scanner = new Scanner(System.in);

                                    if (msg instanceof CommandLOG){
                                        //Если мессейдж - логин, запрашиваем его у пользователя в каком-то месте
                                        System.out.println("введите логин");
                                        login = scanner.next();
                                        command.setContent(login.getBytes());
                                        System.out.println(new String(command.getContent()));
                                        ctx.writeAndFlush(command);
                                    } else if(msg instanceof CommandPAS){
                                        //Если мессейдж - пароль, запрашиваем его у пользователя
                                        System.out.println("Введите пароль");
                                        password = scanner.next();
                                        String pass = login + password;
                                        System.out.println(pass);
                                        command.setContent(AuthUtils.md5(pass).getBytes());
                                        ctx.writeAndFlush(command);
                                    }

                                }
                            });
                        }
                    })
                    .connect("localhost", 8189)
                    .sync()
                    .channel()
                    .closeFuture()
                    .sync();

        } finally {
            group.shutdownGracefully();
        }
    }
}