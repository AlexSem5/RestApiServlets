package ru.alexsem.restapiservlets.config;

import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import ru.alexsem.restapiservlets.models.Event;
import ru.alexsem.restapiservlets.models.File;
import ru.alexsem.restapiservlets.models.User;

public class SessionFactoryClass {
    /**
     * Singleton sessionFactory
     */
    private static SessionFactory sessionFactory;
    
    private SessionFactoryClass() {
    }
    
    /**
     * По умолчанию класс Configuration читает конфигурацию из
     * hibernate.properties
     * @return
     */
    public static SessionFactory getInstance() {
        if (sessionFactory == null) {
            Configuration configuration = new Configuration()
                    .addAnnotatedClass(Event.class)
                    .addAnnotatedClass(File.class)
                    .addAnnotatedClass(User.class);
            sessionFactory = configuration.buildSessionFactory();
        }
        return sessionFactory;
    }
}
