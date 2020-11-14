package cloudusers;


import io.netty.channel.Channel;
import database.entity.User;
import database.services.UserService;

import java.util.HashMap;
import java.util.Map;


/**
 * Пул пользователей.
 * Ключ - соединение пользователя с сервером
 */
public class UserPool {
    //Хэшмапа соединений
    private Map<Channel, User> users = new HashMap<>();

    public Map<Channel, User> getUsers() {
        return users;
    }

    private UserService Service = new UserService();

    //При коннекте
    public void addUser(Channel channel, User user){
        users.put(channel, user);
    }

    //При дисконнекте
    public void removeUser(Channel channel){
        users.remove(channel);
    }

    //Получение пользователя из мапы
    public User getUser(Channel channel){
        return users.get(channel);
    }


    public UserService getUserService() {
        return Service;
    }
}
