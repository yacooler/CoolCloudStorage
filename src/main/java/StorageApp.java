import common.utils.DataUtils;
import server.database.HibernateSessionFactory;
import server.database.entity.User;
import org.hibernate.Session;
import org.hibernate.query.Query;
import server.NettyServer;


import java.util.List;

public class StorageApp {
    public static void main(String[] args) {
        System.out.println("Welcome to CoolCloudStorage!");

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



}
