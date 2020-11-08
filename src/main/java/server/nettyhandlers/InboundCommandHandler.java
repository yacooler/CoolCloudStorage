package server.nettyhandlers;

import commands.*;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import server.database.entity.User;
import server.users.UserPool;
import server.users.ServerUser;

import java.util.Optional;

public class InboundCommandHandler extends ChannelInboundHandlerAdapter {

    private UserPool userPool;


    public InboundCommandHandler(UserPool userPool) {
        this.userPool = userPool;
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        userPool.addUser(ctx.channel(), new ServerUser());
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        userPool.removeUser(ctx.channel());
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        System.out.println("InboundCommandHandler");

        //Получили пользователя, с которым работаем
        ServerUser serverUser = userPool.getUser(ctx.channel());

        //todo вынести обработку в отдельные классы
        if (msg instanceof BaseCommand) {

            BaseCommand response;

            //Если пользователь новый и только коннектится
            if (msg instanceof CommandNEW) {
                System.out.println("new user connected and sent NEW");
                //Отвечаем ему LOG
                response = new CommandLOG("server is waiting for login");
            } else if (msg instanceof CommandLOG){
                CommandLOG log = (CommandLOG) msg;
                System.out.println("user sent login:" + log.getContentAsString());
                Optional<User> optionalUser = userPool.getUserService().findUser(new String(log.getContent()));
                optionalUser.ifPresent(serverUser::setDatabaseUser);
                if (optionalUser.isPresent()) {
                    System.out.println("login OK");
                } else {
                    System.out.println("login FAIL");
                }
                response = new CommandPAS("server is waiting for the password");
            } else if (msg instanceof CommandPAS){
                CommandPAS pas = (CommandPAS) msg;
                System.out.println("user sent password:" + pas.getContentAsString());
                //Если пароль, пришедший от клиента совпал с паролем в базе - вышли в рабочий режим
                if (serverUser.getDatabaseUser().getPassword().equalsIgnoreCase(new String(pas.getContent()))){
                    System.out.println("Ура, мы вычитали из базы для пользователя такой-же пароль!");
                    response = new CommandSCC();
                } else {
                    //иначе запрашиваем логин и пароль заново
                    response = new CommandLOG("fail while authorization");
                }
            } else {
                response = new CommandLOG("unsupportet command or statement");
            }

            if (null != response) {
                if (response.getContent() != null) System.out.println(response.getContentAsString());
                ctx.writeAndFlush(response);
            }
        }

    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        throw new RuntimeException("Ошибка Command Handler", cause);
    }
}
