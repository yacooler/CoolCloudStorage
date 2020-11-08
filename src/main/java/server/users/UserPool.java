package server.users;


import io.netty.channel.Channel;
import server.database.services.UserService;

import java.util.HashMap;
import java.util.Map;


/**
 * Пул пользователей.
 * Ключ - соединение пользователя с сервером
 */
public class UserPool {
    //Хэшмапа соединений
    private Map<Channel, ServerUser> users = new HashMap<>();

    public Map<Channel, ServerUser> getUsers() {
        return users;
    }

    private UserService userService = new UserService();

    //При коннекте
    public void addUser(Channel channel, ServerUser serverUser){
        users.put(channel, serverUser);
    }

    //При дисконнекте
    public void removeUser(Channel channel){
        users.remove(channel);
    }

    //Получение пользователя из мапы
    public ServerUser getUser(Channel channel){
        return users.get(channel);
    }




    public UserService getUserService() {
        return userService;
    }
}
