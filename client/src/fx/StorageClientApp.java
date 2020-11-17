package fx;


import nettyclient.NettyClient;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class StorageClientApp extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{


        FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource("login.fxml"));
        Parent root = loader.load();
        primaryStage.initStyle(StageStyle.UTILITY);
        primaryStage.setTitle("CoolCloudStorage");
        primaryStage.setScene(new Scene(root, 440, 200));
        primaryStage.show();
        primaryStage.setAlwaysOnTop(true);

        System.out.println(Thread.currentThread());

        NettyClient nettyClient;

        new Thread(nettyClient = new NettyClient("localhost", 8189)).start();


        LoginController loginController = loader.getController();
        loginController.setNettyClient(nettyClient);

    }


    public static void main(String[] args) {
        launch(args);
    }
}
