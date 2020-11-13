package common.frames;

import io.netty.channel.ChannelHandlerContext;
import server.cloudusers.UserPool;
import server.database.entity.User;

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
