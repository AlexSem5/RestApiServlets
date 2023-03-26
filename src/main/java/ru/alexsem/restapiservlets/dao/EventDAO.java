package ru.alexsem.restapiservlets.dao;

import org.hibernate.Hibernate;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import ru.alexsem.restapiservlets.config.SessionFactoryClass;
import ru.alexsem.restapiservlets.models.Event;
import ru.alexsem.restapiservlets.models.File;
import ru.alexsem.restapiservlets.models.User;

import java.util.*;

/**
 * Singleton
 */
public class EventDAO {
    private static SessionFactory sessionFactory;
    private static EventDAO eventDAO;
    
    private EventDAO() {
    }
    
    //        По умолчанию класс Configuration читает конфигурацию из hibernate.properties
    public static EventDAO getInstance() {
        sessionFactory = SessionFactoryClass.getInstance();
        if (eventDAO == null) {
            eventDAO = new EventDAO();
        }
        return eventDAO;
    }
    
    public List<Event> index() {
//    Сессия для работы с Hibernate
        Session session = sessionFactory.getCurrentSession();
        session.beginTransaction();
//       or session.createQuery("from Person");
        List<Event> events = session.createQuery("select e from Event e", Event.class)
                                    .getResultList();
//    когда происходит коммит транзакции hibernate автоматически
//    вызывает session.close() и закрывает текущую сессию.
        session.getTransaction().commit();
        return events;
    }
    
    public Event show(int id) {
        Session session = sessionFactory.getCurrentSession();
        session.beginTransaction();
        Event event = session.get(Event.class, id);
        session.getTransaction().commit();
        return event;
    }
    
    public void save(Event event) {
        Session session = sessionFactory.getCurrentSession();
        session.beginTransaction();
        session.save(event);
//  С двух сторон вносим изменения, чтобы в кэш была актуальная инф-ция
        event.getFile().setEvent(event);
        if (event.getUser().getEvents() == null) {
            event.getUser().setEvents(
                    new ArrayList<>(Collections.singletonList(event)));
        } else {
            event.getUser().getEvents().add(event);
        }
        session.getTransaction().commit();
    }
    
    /**
     * Обновляем сущность Event
     *
     * @param id           id текушей сущности
     * @param updatedEvent берём обновлённые данные из этого объекта
     */
    
    public void update(int id, Event updatedEvent) {
        Session session = sessionFactory.getCurrentSession();
        session.beginTransaction();
        Event eventToBeUpdated = session.get(Event.class, id);
//        Находимся внутри транзакции. Объект находится в состоянии persistent(managed) -
//        в области persistent context. Вызов сеттера породит SQL-запрос.
        eventToBeUpdated.setFile(updatedEvent.getFile());
        eventToBeUpdated.setUser(updatedEvent.getUser());
//            С двух сторон вносим изменения, чтобы в кэш была актуальная инф-ция
        updatedEvent.getFile().setEvent(eventToBeUpdated);
        if (updatedEvent.getUser().getEvents() != null) {
            updatedEvent.getUser().getEvents().add(eventToBeUpdated);
        }
        session.getTransaction().commit();
    }
    
    /**
     * Удаляем Event с учётом каскадирования в бд (on delete cascade)
     *
     * @param id event id
     */
    public void delete(int id) {
        Session session = sessionFactory.getCurrentSession();
        session.beginTransaction();
        Event event = session.get(Event.class, id);
//      Команда породит SQL запрос к БД (каскадирование?)
        session.remove(event);
//       С двух сторон вносим изменения, чтобы в кэш была актуальная инф-ция
        event.getFile().setEvent(null);
        event.getUser().getEvents().remove(event);
        session.getTransaction().commit();
    }
}
