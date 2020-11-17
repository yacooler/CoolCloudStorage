package nettyclient;


import fileobjects.FileList;
import frames.BaseFrame;
import frames.CommandNEW;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.UnsupportedMessageTypeException;
import io.netty.handler.codec.serialization.ClassResolvers;
import io.netty.handler.codec.serialization.ObjectDecoder;
import io.netty.handler.codec.serialization.ObjectEncoder;
import javafx.util.Callback;

import java.io.OutputStream;


public class NettyClient implements Runnable{

    private final String inetHost;
    private final int inetPort;
    private Callback<BaseFrame, BaseFrame> receivedMessageHandler;


    private FileList fileList;
    private OutputStream outputStream;


    //todo currentHandlerContext.close()
    private ChannelHandlerContext currentHandlerContext;

    //Группы операций
    private AuthorizationLoop authorizationLoop;


    public NettyClient(String inetHost, int inetPort){
        this.inetHost = inetHost;
        this.inetPort = inetPort;

    }


    public void setReceivedMessageHandler(Callback<BaseFrame, BaseFrame> receivedMessageHandler) {
        this.receivedMessageHandler = receivedMessageHandler;
    }


    public void mainLoop() throws Exception {

        NioEventLoopGroup group = new NioEventLoopGroup();
        try {
            new Bootstrap().group(group)
                    .channel(NioSocketChannel.class)
                    .handler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        public void initChannel(SocketChannel socketChannel) {
                            ChannelPipeline pipeline = socketChannel.pipeline();
                            pipeline.addLast(new ObjectEncoder());
                            pipeline.addLast(new ObjectDecoder(ClassResolvers.cacheDisabled(null)));
                            pipeline.addLast(new ChannelInboundHandlerAdapter(){

                                @Override
                                public void channelActive(ChannelHandlerContext ctx) {
                                    //Пользователь подключился
                                    System.out.println("Клиент подключился к серверу");
                                    currentHandlerContext = ctx;
                                    authorizationLoop = new AuthorizationLoop(currentHandlerContext);
                                    System.out.println(authorizationLoop);
                                    ctx.writeAndFlush(new CommandNEW());
                                    System.out.println(Thread.currentThread());
                                }

                                @Override
                                public void channelRead(ChannelHandlerContext ctx, Object msg) {
                                    BaseFrame response;
                                    BaseFrame command;
                                    System.out.println(Thread.currentThread());
                                    //Если пока не подключен обработчик события - ничего не делаем
                                    if (receivedMessageHandler == null) return;

                                    if (msg instanceof BaseFrame) {
                                        command = (BaseFrame) msg;
                                        response = receivedMessageHandler.call(command);
                                        if (response != null) {
                                            System.out.println(response);
                                            ctx.writeAndFlush(response);
                                        }
                                    } else {
                                        throw new UnsupportedMessageTypeException("Не удалось обработать сообщение. Ожидается объект класса BaseFrame!");
                                    }
                                };
                            });
                        }
                    })
            .connect(inetHost, inetPort)
                    .sync()
                    .channel()
                    .closeFuture()
                    .sync();

        } finally {
            group.shutdownGracefully();
        }
    }


    @Override
    public void run() {
        try {
            mainLoop();
        } catch (Exception e) {
            throw new RuntimeException("Ошибка при работе клиентского потока", e);
        }
    }

    //Передаем логин и пароль, назначаем объект слушателем сообщений, запускаем
    public boolean doAuthorization(String login, String password) {
        authorizationLoop.setLogPass(login, password);
        setReceivedMessageHandler(authorizationLoop);
        authorizationLoop.setReadyToAuth(true);

        System.out.println("Запустили авторизацию из потока " + Thread.currentThread());
        while (authorizationLoop.isReadyToAuth()){
            try {
                Thread.sleep(100);
            } catch (InterruptedException interruptedException) {
                interruptedException.printStackTrace();
            }
        }

        return (authorizationLoop.isAuthorized());
    }


}