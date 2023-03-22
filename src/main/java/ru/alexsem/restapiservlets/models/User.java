package ru.alexsem.restapiservlets.models;

import lombok.*;
import org.hibernate.annotations.Cascade;

import javax.persistence.*;
import java.util.List;

/**
 * Entities:
 * User -> Integer id, String name, List<Event> events
 * Event -> Integer id, User user, File file
 * File -> Integer id, String name, String filePath, Event event
 *
 * One-to-many relationship: User-Events
 */
@Entity
@Table(name = "User")
@Data
@NoArgsConstructor
public class User {
    @Id
    @Column(name = "id")
//  id генерируется на стороне бд
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    
    @Column(name = "name")
    private String name;
    
//  Название поля с аннотацией @JoinColumn в доч таблице:
//  (cсылаемся на поле owning side)
    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY)
    //Если сохраняем человека, то автоматически сохраняется связанная сущность
//    Можем добавить каскадир других операций (on delete уже настроено на стороне БД)
    @Cascade(org.hibernate.annotations.CascadeType.SAVE_UPDATE)
    private List<Event> events;
    
    public User(String name) {
        this.name = name;
    }
    
    /**
     * В DAO в каждом круд методе нужно будет открывать и закрывать
     * транзакцию.
     * По умолчанию класс Configuration читает конфигурацию из hibernate.properties
     * Вынести в отдельный метод:
     * Configuration configuration = new Configuration().addAnnotatedClass(Person.class);
     *         SessionFactory sessionFactory = configuration.buildSessionFactory();
     *         Вернуть sessionFactory и использовать во всех методах
     *         (см класс PersonDAO в SpringHiberApp)
     *
     *         sessionFactory.close() в отдельный блок finally или метод
     *         в конце?
     */
}
