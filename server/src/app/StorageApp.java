package app;

public class StorageApp {
    public static void main(String[] args) {
        System.out.println("Welcome to CoolCloudStorage!");

        startServer();


    }

    public static void startServer(){
        NettyServer server = new NettyServer();
    }

}
