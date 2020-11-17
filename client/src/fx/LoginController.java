package fx;


import nettyclient.NettyClient;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

import java.net.URL;
import java.util.ResourceBundle;

public class LoginController implements Initializable {

    NettyClient nettyClient;

    public void setNettyClient(NettyClient nettyClient) {
        this.nettyClient = nettyClient;
    }

    @FXML // fx:id="passwordTextField"
    private PasswordField passwordTextField;

    @FXML // fx:id="loginButton"
    private Button loginButton;

    @FXML // fx:id="loginTextField"
    private TextField loginTextField;

    @FXML
    private Label messageLabel;


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        loginTextField.requestFocus();

    }

    public void setMessageText(String text){
        messageLabel.setText(text);
    }

    @FXML
    void doLogin(ActionEvent event) {

        if (loginTextField.getText().isBlank() || passwordTextField.getText().isBlank()){
            setMessageText("Введите логин и пароль!");
            return;
        }

        //Пробуем авторизоваться
        if (nettyClient.doAuthorization(loginTextField.getText(),  passwordTextField.getText())) {
            setMessageText("Авторизация прошла успешно!");
        } else {
            setMessageText("Проверьте правильность логина и пароля!");
        };


    }



}