package ru.alexsem.restapiservlets.config;

import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import ru.alexsem.restapiservlets.models.Event;
import ru.alexsem.restapiservlets.models.File;
import ru.alexsem.restapiservlets.models.User;

/**
 * Singleton
 */
public class SessionFactoryClass {
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
            FlywayClass.getInstance().migrate();
            Configuration configuration = new Configuration()
                    .addAnnotatedClass(User.class)
                    .addAnnotatedClass(Event.class)
                    .addAnnotatedClass(File.class);
            sessionFactory = configuration.buildSessionFactory();
        }
        return sessionFactory;
    }
}
