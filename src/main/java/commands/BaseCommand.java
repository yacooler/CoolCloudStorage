package commands;

import java.io.Serializable;


/**
 * Базовый класс "Пакет", посредством которого передаются данные с клиента на сервер и обратно
 */


public class BaseCommand implements Serializable {

    private static final long serialVersionUID = 1L;

    protected byte commandId;
    protected byte[] semantic;

    protected long commandDataSize;
    byte[] content;


    public BaseCommand() {
        afterConstruct();
    }

    public BaseCommand(byte[] content) {
        this.content = content;
        System.out.println(new String(content));
        afterConstruct();
    }

    public BaseCommand(String content) {
        this.content = content.getBytes();
        System.out.println(new String(content));
        afterConstruct();
    }

    public void setContent(byte[] content) {
        this.content = content;
    }

    public byte[] getContent(){
        return this.content;
    }

    public String getContentAsString(){
        return content.length > 0 ? new String(content) : "";
    }

    public void setContentAsString(String content){
        this.content = content.getBytes();
    }

    protected void afterConstruct(){}

}
