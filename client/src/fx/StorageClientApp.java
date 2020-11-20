package fx;


import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import nettyclient.NettyClient;

import java.io.IOException;
import java.net.URL;

public class StorageClientApp extends Application {
    private Stage primaryStage;
    private FXMLLoader loader;
    private NettyClient nettyClient;

    private final String LOGIN_SCENE = "login.fxml";
    private final String FILE_MANAGER_SCENE = "login.fxml";


    @Override
    public void start(Stage primaryStage) throws Exception{



        new Thread(nettyClient = new NettyClient("localhost", 8189)).start();

        //loader = new FXMLLoader(getClass().getClassLoader().getResource("login.fxml"));

        //Parent root = loader.load();
        //changeScene(LOGIN_SCENE);

        this.primaryStage = primaryStage;

        login();

        primaryStage.initStyle(StageStyle.UTILITY);
       // primaryStage.setTitle("CoolCloudStorage");
       // primaryStage.setScene(new Scene(root, 440, 200));
//        primaryStage.show();
//        primaryStage.setAlwaysOnTop(true);
//
//
//        LoginController loginController = loader.getController();
//        loginController.setNettyClient(nettyClient);

    }

    public void login(){
        changeScene(LOGIN_SCENE);
        primaryStage.initStyle(StageStyle.UTILITY);
        primaryStage.setAlwaysOnTop(true);
        LoginController loginController = loader.getController();
        loginController.setNettyClient(nettyClient);
    }


    public void changeScene(String scene){

        if (primaryStage != null) primaryStage.hide();

        Parent pane = null;
        try {
            pane = FXMLLoader.load(getClass().getClassLoader().getResource(scene));
        } catch (IOException e) {
            throw new RuntimeException("Не удалось загрузить ресурс для сцены " + scene, e);
        }
        primaryStage.setScene(new Scene(pane));
        primaryStage.show();
    }

    private URL getResource(String sceneName){
        return getClass().getClassLoader().getResource(sceneName);
    }

    public static void main(String[] args) {
        launch(args);
    }
}
