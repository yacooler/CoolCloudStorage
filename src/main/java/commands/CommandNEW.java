package commands;

public class CommandNEW extends BaseCommand{
    public CommandNEW() {
        super();
    }

    public CommandNEW(byte[] content) {
        super(content);
    }

    public CommandNEW(String content) {
        super(content);
    }

    @Override
    protected void afterConstruct() {
        semantic = "NEW".getBytes();
    }
}
