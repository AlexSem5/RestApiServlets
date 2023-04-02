package ru.alexsem.restapiservlets.dao;

import org.hibernate.Hibernate;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;
import ru.alexsem.restapiservlets.config.SessionFactoryClass;
import ru.alexsem.restapiservlets.models.Event;
import ru.alexsem.restapiservlets.models.File;
import ru.alexsem.restapiservlets.models.User;

import javax.persistence.EntityManagerFactory;
import javax.persistence.PersistenceUnit;
import java.util.ArrayList;
import java.util.List;

/**
 * Singleton
 */
public class UserDAO {
    /**
     * ВОПРОС: Где закрывается sessionFactory (sessionFactory.close())?
     * Поля должны быть final?
     */
    private static SessionFactory sessionFactory;
    private static UserDAO userDAO;
    
    private UserDAO() {
    }
    
    //        По умолчанию класс Configuration читает конфигурацию из hibernate.properties
    public static UserDAO getInstance() {
        sessionFactory = SessionFactoryClass.getInstance();
        if (userDAO == null) {
            userDAO = new UserDAO();
        }
        return userDAO;
    }
    
    public List<User> index() {
        Session session = sessionFactory.openSession();
        List<User> users = null;
        try {
            session.beginTransaction();
//     or session.createQuery("from Person");
            users = session.createQuery("select u from User u", User.class)
                                      .getResultList();
            session.getTransaction().commit();
        } catch (RuntimeException e) {
                session.getTransaction().rollback();
                e.printStackTrace();
        } finally {
            session.close();
        }
        return users;
    }
    
    public User show(int id) {
        Session session = sessionFactory.openSession();
        User user = null;
        try {
            session.beginTransaction();
            user = session.get(User.class, id);
            session.getTransaction().commit();
        } catch (RuntimeException e) {
            session.getTransaction().rollback();
            e.printStackTrace();
        } finally {
            session.close();
        }
        return user;
    }
    
    public void save(User user) {
        Session session = sessionFactory.openSession();
        try {
            session.beginTransaction();
            session.save(user);
            session.getTransaction().commit();
        } catch (RuntimeException e) {
            session.getTransaction().rollback();
            e.printStackTrace();
        } finally {
            session.close();
        }
    }
    
    /**
     * Consider a utility method which updates both entities
     * для добавления и обновления сущности используется один и тот же метод save(person)
     *
     * @param id          id текушей сущности
     * @param updatedUser берём обновлённые данные из этого объекта
     */
    public void update(int id, User updatedUser) {
        Session session = sessionFactory.openSession();
        try {
            session.beginTransaction();
            updatedUser.setId(id);
//            Обновит значения у существующего человека (по id найдёт его):
            session.save(updatedUser);
            session.getTransaction().commit();
        } catch (RuntimeException e) {
            session.getTransaction().rollback();
            e.printStackTrace();
        } finally {
            session.close();
        }
    }
    
    /**
     * Удаляем User с учётом каскадирования в бд (on delete cascade)
     *
     * @param id user id
     */
    public void delete(int id) {
        Session session = sessionFactory.openSession();
        try {
            session.beginTransaction();
            User user = session.get(User.class, id);
//      Команда породит SQL запрос к БД, она сделает каскадирование и удалит
//      связанные записи в БД:
            session.remove(user);
//      Сделаем, чтобы в кэш Hibernate каждая связанная сущность была удалена:
            user.getEvents().clear();
            session.getTransaction().commit();
        } catch (RuntimeException e) {
            session.getTransaction().rollback();
            e.printStackTrace();
        } finally {
            session.close();
        }
    }
}