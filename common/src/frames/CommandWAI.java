package frames;

import cloudusers.UserPool;
import database.entity.User;
import io.netty.channel.ChannelHandlerContext;

public class CommandWAI extends BaseCommandFrame{
    public CommandWAI() {
    }

    /**
     * Команда, указывающая, что сервер ожидает команды от клиента, либо клиент - от сервера
     */
    @Override
    public BaseFrame serverProcessing(ChannelHandlerContext ctx, UserPool userPool, User user) {
        return new CommandWAI();
    }
}
