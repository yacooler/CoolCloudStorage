package commands;

public class CommandLOG extends BaseCommand{
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
}
