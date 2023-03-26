package ru.alexsem.restapiservlets.dao;

import org.hibernate.Hibernate;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import ru.alexsem.restapiservlets.config.SessionFactoryClass;
import ru.alexsem.restapiservlets.models.Event;
import ru.alexsem.restapiservlets.models.File;
import ru.alexsem.restapiservlets.models.User;

import java.util.ArrayList;
import java.util.List;

/**
 * Singleton
 */
public class UserDAO {
    /**
     * ВОПРОС: Где закрывается sessionFactory (sessionFactory.close())?
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
//    Сессия для работы с Hibernate
        Session session = sessionFactory.getCurrentSession();
        session.beginTransaction();
//       or session.createQuery("from Person");
        List<User> users = session.createQuery("select u from User u", User.class)
                                  .getResultList();
//    когда происходит коммит транзакции hibernate автоматически
//    вызывает session.close() и закрывает текущую сессию.
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
//        user.setEvents(new ArrayList<>()); - сделал на стороне Event. Здесь почему-то не срабатывает (nullPointerEx)
        session.getTransaction().commit();
    }
    
    /**
     * Обновляем сущность User
     *
     * @param id          id текушей сущности
     * @param updatedUser берём обновлённые данные из этого объекта
     */
    public void update(int id, User updatedUser) {
        Session session = sessionFactory.getCurrentSession();
        session.beginTransaction();
        User userToBeUpdated = session.get(User.class, id);
//        Находимся внутри транзакции. Объект находится в состоянии persistent(managed) -
//        в области persistent context. Вызов сеттера породит SQL-запрос.
        userToBeUpdated.setName(updatedUser.getName());
        if (updatedUser.getEvents() != null) {
            userToBeUpdated.setEvents(updatedUser.getEvents());
//            С двух сторон вносим изменения, чтобы в кэш была актуальная инф-ция
            updatedUser.getEvents().forEach(event -> event.setUser(userToBeUpdated));
        } else {
            userToBeUpdated.setEvents(new ArrayList<>());
        }
        session.getTransaction().commit();
    }
    
    /**
     * Удаляем User с учётом каскадирования в бд (on delete cascade)
     *
     * @param id user id
     */
    public void delete(int id) {
        Session session = sessionFactory.getCurrentSession();
        session.beginTransaction();
        User user = session.get(User.class, id);
//      Команда породит SQL запрос к БД, она сделает каскадирование и удалит
//      связанные записи в БД:
        session.remove(user);
//       Сделаем, чтобы в кэш Hibernate каждая связанная сущность была удалена:
        user.getEvents().clear();
        session.getTransaction().commit();
    }
}