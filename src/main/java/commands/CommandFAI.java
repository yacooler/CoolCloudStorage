package commands;

public class CommandFAI extends BaseCommand{
    public CommandFAI() {
        super();
    }

    public CommandFAI(byte[] content) {
        super(content);
    }

    public CommandFAI(String content) {
        super(content);
    }

    @Override
    protected void afterConstruct() {
        semantic = "FAI".getBytes();
    }
}
