import database.HibernateSessionFactory;
import database.entity.User;
import org.hibernate.Session;
import org.hibernate.query.Query;
import server.NettyServer;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.List;

public class StorageApp {
    public static void main(String[] args) {
        System.out.println("Welcome to CoolCloudStorage!");
        try {
            System.out.println(md5("coolerpassword123"));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        startServer();

    }

    public static void startServer(){
        NettyServer server = new NettyServer();
    }


    public static void check(){
        Session session = HibernateSessionFactory.getSessionFactory().openSession();

        Query query = session.createQuery("From User where name = :name").setParameter("name", "cooler");

        List<User> list = (List<User>) query.list();

        if (!list.isEmpty()) {list.forEach(System.out::println);}

        session.close();


    }

    //Переедет в автотирацию
    public static String md5(String string) throws UnsupportedEncodingException, NoSuchAlgorithmException {
        //Результат совпадает с postgresql select md5('...')
        StringBuilder stringBuilder = new StringBuilder();

        //Получаем массив байт в md5 и переводим его в строку, т.к. в бд он лежит в строке
        byte[] bytes = MessageDigest.getInstance("MD5").digest(string.getBytes());

        for (int i = 0; i < bytes.length; i += 2) {
            stringBuilder.append(Integer.toHexString((bytes[i] & 0xff) << 8 | (bytes[i+1] & 0xff)));
        }

        return stringBuilder.toString();
    }

}
