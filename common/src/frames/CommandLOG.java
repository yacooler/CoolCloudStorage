package frames;

import cloudusers.UserPool;
import database.entity.User;
import io.netty.channel.ChannelHandlerContext;

import java.util.Optional;

public class CommandLOG extends BaseCommandFrame {
    public CommandLOG() {
        super();
    }

    public CommandLOG(byte[] content) {
        super(content);
    }

    public CommandLOG(String content) {
        super(content);
    }

    @Override
    protected void afterConstruct() {
        semantic = "LOG".getBytes();
    }

    /**
     *В случае, если клиент прислал логин - пытаемся найти пользователя с таким логином
     * По бизнес-процессу ищем этого пользователя в БД, а не в пуле, т.к. это может
     * быть новое соединение с другого устройства
     */

    @Override
    public BaseFrame serverProcessing(ChannelHandlerContext ctx, UserPool userPool, User user){
        System.out.println("user sent login:" + getContentAsString());

        //Если логин совпал - вычитали пользователя из БД
        Optional<User> optionalUser = userPool.getUserService().findUser(getContentAsString());
        if (optionalUser.isPresent()) {
            System.out.println("login OK");
            userPool.addUser(ctx.channel(), optionalUser.get());
        } else {
            System.out.println("login FAIL");
        }
        //Пароль запрашиваем в любом случае, чтобы избежать атаки перебором на логины
        return new CommandPAS("Server is waiting for the password");
    }
}
