package common.frames;

import io.netty.channel.ChannelHandlerContext;
import server.cloudusers.UserPool;
import server.database.entity.User;

public class CommandPAS extends BaseCommandFrame {
    public CommandPAS() {
        super();
    }

    public CommandPAS(byte[] content) {
        super(content);
    }

    public CommandPAS(String content) {
        super(content);
    }

    @Override
    protected void afterConstruct() {
        semantic = "PAS".getBytes();
    }

    @Override
    public BaseFrame serverProcessing(ChannelHandlerContext ctx, UserPool userPool, User user){
        System.out.println("user sent password:" + getContentAsString());
        //Если пароль, пришедший от клиента совпал с паролем в базе - вышли в рабочий режим
        if (user.getPassword().equalsIgnoreCase(getContentAsString())){
            System.out.println("Ура, мы вычитали из базы для пользователя такой-же пароль!");
            return new CommandSCC();
        }
        //иначе запрашиваем логин и пароль заново
        return new CommandLOG("fail while authorization");
    }
}
