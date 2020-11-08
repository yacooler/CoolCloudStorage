package commands;


/**
 * Success. Команда отправляется в случае, если предыдущая операция завершена успешно
 */
public class CommandSCC extends BaseCommand{
    public CommandSCC() {
        super();
    }

    public CommandSCC(byte[] content) {
        super(content);
    }

    public CommandSCC(String content) {
        super(content);
    }

    @Override
    protected void afterConstruct() {
        semantic = "SCC".getBytes();
    }
}
