package ru.alexsem.restapiservlets.dao;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import ru.alexsem.restapiservlets.config.SessionFactoryClass;
import ru.alexsem.restapiservlets.models.File;
import ru.alexsem.restapiservlets.models.User;

import java.util.ArrayList;
import java.util.List;

/**
 * Singleton
 */
public class FileDAO {
    private static SessionFactory sessionFactory;
    
    private static FileDAO fileDAO;
    
    private FileDAO() {
    }
    
    //        По умолчанию класс Configuration читает конфигурацию из hibernate.properties
    public static FileDAO getInstance() {
        sessionFactory = SessionFactoryClass.getInstance();
        if (fileDAO == null) {
            fileDAO = new FileDAO();
        }
        return fileDAO;
    }
    
    public List<File> index() {
//    Сессия для работы с Hibernate
        Session session = sessionFactory.getCurrentSession();
        session.beginTransaction();
//       or session.createQuery("from File");
        List<File> files = session.createQuery("select f from File f", File.class)
                                  .getResultList();
//    когда происходит коммит транзакции hibernate автоматически
//    вызывает session.close() и закрывает текущую сессию.
        session.getTransaction().commit();
        return files;
    }
    
    public File show(int id) {
        Session session = sessionFactory.getCurrentSession();
        session.beginTransaction();
        File file = session.get(File.class, id);
        session.getTransaction().commit();
        return file;
    }
    
    public void save(File file) {
        Session session = sessionFactory.getCurrentSession();
        session.beginTransaction();
        session.save(file);
        session.getTransaction().commit();
    }
    
    /**
     * Обновляем сущность File
     *
     * @param id          id текушей сущности
     * @param updatedFile берём обновлённые данные из этого объекта
     */
    public void update(int id, File updatedFile) {
        Session session = sessionFactory.getCurrentSession();
        session.beginTransaction();
        File fileToBeUpdated = session.get(File.class, id);
//        Находимся внутри транзакции. Объект находится в состоянии persistent(managed) -
//        в области persistent context. Вызов сеттера породит SQL-запрос.
        fileToBeUpdated.setName(updatedFile.getName());
        fileToBeUpdated.setFilePath(updatedFile.getFilePath());
        if (updatedFile.getEvent() != null) {
            fileToBeUpdated.setEvent(updatedFile.getEvent());
//            С двух сторон вносим изменения, чтобы в кэш была актуальная инф-ция
            updatedFile.getEvent().setFile(fileToBeUpdated);
        }
        session.getTransaction().commit();
    }
    
    /**
     * Удаляем File с учётом каскадирования в бд (on delete cascade)
     *
     * @param id file id
     */
    public void delete(int id) {
        Session session = sessionFactory.getCurrentSession();
        session.beginTransaction();
        File file = session.get(File.class, id);
//      Команда породит SQL запрос к БД, она сделает каскадирование и удалит
//      связанные записи в БД (Event):
        session.remove(file);
//       Сделаем, чтобы в кэш Hibernate каждая связанная сущность была удалена:
        if (file.getEvent() != null) {
            file.getEvent().setFile(null);
        }
        session.getTransaction().commit();
    }
}




