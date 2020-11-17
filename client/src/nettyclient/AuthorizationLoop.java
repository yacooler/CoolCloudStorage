package nettyclient;

import frames.BaseFrame;
import frames.CommandLOG;
import frames.CommandPAS;
import frames.CommandSCC;
import io.netty.channel.ChannelHandlerContext;
import javafx.util.Callback;
import utils.AuthUtils;


public class AuthorizationLoop implements Callback<BaseFrame, BaseFrame> {
    private final ChannelHandlerContext channelHandlerContext;
    private boolean readyToAuth;
    private String login;
    private String password;
    private boolean authorized;


    public void setReadyToAuth(boolean readyToAuth) {
        this.readyToAuth = readyToAuth;

        //Запускаем цикл логина
        if (readyToAuth){
            //Запускаем цикл с логином
            channelHandlerContext.writeAndFlush(call(new CommandLOG(login)));
        }
    }

    public boolean isReadyToAuth() {
        return readyToAuth;
    }

    public AuthorizationLoop(ChannelHandlerContext channelHandlerContext) {
        this.channelHandlerContext = channelHandlerContext;
    }

    public boolean setLogPass(String login, String password){
        this.login = login;
        this.password = password;
        return false;
    }

    public boolean isChannelContextActive(){
        return (channelHandlerContext != null);
    }

    @Override
    public BaseFrame call(BaseFrame baseFrame) {

        System.out.println(baseFrame.getContentAsString());

        //Если мы не готовы к авторизации по какой-то причине - выходим
        if (!readyToAuth) return null;

        //Разбираем пакеты
        if (baseFrame instanceof CommandLOG){
            CommandLOG commandLOG = (CommandLOG) baseFrame;
            if (commandLOG.getContentAsString().equalsIgnoreCase(CommandPAS.FAIL_WHILE_AUTH)){
                //Не удалось приконнектиться с предыдущим логином и паролем - сбрасываем признак готовности к авторизации
                //и не отправляем на сервер логин до повторной активации
                readyToAuth = false;
                authorized = false;
                return null;
            }
            System.out.println("Отправляем на сервер логин");
            return commandLOG;
        } else if(baseFrame instanceof CommandPAS){
            CommandPAS commandPAS = new CommandPAS(AuthUtils.md5(login + password).getBytes());
            System.out.println("Отправляем на сервер пароль");
            return commandPAS;
        } else if(baseFrame instanceof CommandSCC){
            //Если логин и пароль совпали
            this.authorized = true;
            readyToAuth = false;
        }
        return baseFrame;
    }

    public boolean isAuthorized() {
        return authorized;
    }


}
