package TestClient;

import common.fileobjects.FileList;
import common.fileobjects.FileParameters;
import common.frames.*;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.serialization.ClassResolvers;
import io.netty.handler.codec.serialization.ObjectDecoder;
import io.netty.handler.codec.serialization.ObjectEncoder;
import common.utils.AuthUtils;

import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Scanner;

public class TestClient1 {
    private static String login;
    private static String password;
    private static FileList fileList;
    private static OutputStream outputStream;
    private static Scanner scanner = new Scanner(System.in);

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
                                    BaseFrame command = (BaseFrame) msg;
                                    BaseFrame response;


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
                                    } else if(msg instanceof CommandSCC){
                                        System.out.println("Запрос списка файлов корневой директории");
                                        ctx.writeAndFlush(new CommandDIR());
                                    } else if(msg instanceof CommandDIR){
                                        CommandDIR commandDIR = (CommandDIR) msg;
                                        fileList = commandDIR.getFileList();
                                        for (FileParameters fileParameters : fileList.getFiles()) {
                                            System.out.println(fileParameters);
                                        }
                                        ctx.writeAndFlush(new CommandWAI());

                                    } else if(msg instanceof DataFrameTRN) {
                                        //Получение файла
                                        DataFrameTRN dataFrameTRN = (DataFrameTRN) msg;
                                        dataFrameTRN = dataFrameTRN;
                                        //System.out.println("DataFrameTRN");
                                        outputStream.write(
                                                dataFrameTRN.getContent(),
                                                0,
                                                dataFrameTRN.getContentDataSize()
                                        );

                                        //Если всё скачали
                                        if (dataFrameTRN.getCurrentFrame() == dataFrameTRN.getLastFrame()){
                                            outputStream.close();
                                            outputStream = null;
                                            System.out.println("WAIT");
                                            ctx.writeAndFlush(new CommandWAI());
                                        } else {
                                            //Иначе запрашиваем следующий кадр
                                            CommandGET commandGET = new CommandGET(dataFrameTRN.getCurrentFrame() + 1);
                                            //параметры файла
                                            commandGET.setFileParameters(dataFrameTRN.getFileParameters());

                                            ctx.writeAndFlush(commandGET);
                                        }



                                        //В этот момент outFileStream открыт

//                                        var writer = Files.newOutputStream()
//                                        for (int i = 0; i <= dataFrameTRN.getLastFrame(); i++) {
//
//                                        }
//                                        postContent.setLastFrame((int) fileParameters.getSize() / DATA_SIZE + 1);
//                                        var reader = Files.newInputStream(Path.of(fileName));
//                                        int readed = 0;
//                                        //Пока есть что читать
//                                        while((readed = reader.read(postContent.content)) > 0){
//                                            postContent.contentDataSize = readed;
//                                            ctx.writeAndFlush(postContent);
//                                        }
                                        //Если передача только началась


                                    } else {
                                        System.out.println("Выберите файл на сервере:");
                                        for (int i = 0; i < fileList.getFiles().size(); i++) {
                                            System.out.println(String.format("[%d] %s", i, fileList.getFiles().get(i)));
                                        }
                                        int fileNumber = scanner.nextInt();
                                        var fileParam = fileList.getFiles().get(fileNumber);


                                        //Выбрали файл, запрашиваем первый фрейм, дальше запросы идут через DataFrame
                                        if (fileParam != null) {
                                            System.out.println("client/" + fileParam.getName());
                                            outputStream = Files.newOutputStream(Path.of("client/" + fileParam.getName()));
                                        }
                                        //С первого кадра
                                        CommandGET commandGET = new CommandGET(0);
                                        //Файл
                                        commandGET.setFileParameters(fileParam);
                                        //Передаем параметры файла
                                        ctx.writeAndFlush(commandGET);
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