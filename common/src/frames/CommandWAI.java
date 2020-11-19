package frames;

import io.netty.channel.ChannelHandlerContext;

public class CommandWAI extends BaseCommandFrame{
    public CommandWAI() {
    }

    /**
     * Команда, указывающая, что сервер ожидает команды от клиента, либо клиент - от сервера
     */
    @Override
    public BaseFrame processing(ChannelHandlerContext ctx) {
        return new CommandWAI();
    }
}
