package commands;

public class CommandPAS extends BaseCommand{
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
}
