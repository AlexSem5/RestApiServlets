package ru.alexsem.restapiservlets.dao;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import ru.alexsem.restapiservlets.config.SessionFactoryClass;
import ru.alexsem.restapiservlets.models.Event;
import ru.alexsem.restapiservlets.models.File;
import ru.alexsem.restapiservlets.models.User;

import java.util.List;

/**
 * Singletone class
 */
public class UserDAO {
    /**
     * Где закрывать sessionFactory (sessionFactory.close())?
     */
    private static SessionFactory sessionFactory;
    private static UserDAO userDAO;
    //        По умолчанию класс Configuration читает конфигурацию из hibernate.properties
//    private UserDAO() {
//        sessionFactory = SessionFactoryClass.getInstance();
//    }
    
    private UserDAO() {
    }
    
    public static UserDAO getInstance() {
        sessionFactory = SessionFactoryClass.getInstance();
        if (userDAO == null) {
            userDAO = new UserDAO();
        }
        return userDAO;
    }
    
    public List<User> index() {
//    Сессия для работы с Hibernate
        Session session = sessionFactory.getCurrentSession();
        session.beginTransaction();
//       or session.createQuery("from Person");
        List<User> users = session.createQuery("select u from User u", User.class)
                                  .getResultList();
        session.getTransaction().commit();
        return users;
    }
    
    public User show(int id) {
        Session session = sessionFactory.getCurrentSession();
        session.beginTransaction();
        User user = session.get(User.class, id);
        session.getTransaction().commit();
        return user;
    }
    
    public void save(User user) {
        Session session = sessionFactory.getCurrentSession();
        session.beginTransaction();
        session.save(user);
        session.getTransaction().commit();
    }
    
    public void update(int id, User updatedUser) {
        Session session = sessionFactory.getCurrentSession();
        session.beginTransaction();
        User userToBeUpdated = session.get(User.class, id);
//        Находимся внутри транзакции. Объект находится в состоянии persistent(managed) -
//        в области persistent context
        userToBeUpdated.setName(updatedUser.getName());
        session.getTransaction().commit();
    }
    
    public void delete(int id) {
        Session session = sessionFactory.getCurrentSession();
        session.beginTransaction();
        User user = session.get(User.class, id);
        session.remove(user);
        session.getTransaction().commit();
    }
}