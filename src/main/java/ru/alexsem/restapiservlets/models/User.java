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
@Getter @Setter
@NoArgsConstructor
public class User {
    @Id
    @Column(name = "id")
//  id генерируется на стороне бд
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    
    @Column(name = "name")
    private String name;
    
//  Lazy - связанные товары загрузятся только по запросу (вызов геттера)
//  Ссылаемся на поле owning side с аннотацией @JoinColumn в доч таблице
    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY)
    //Если сохраняем человека, то автоматически сохраняется связанная сущность
//    Можем добавить каскадир других операций (on delete уже настроено на стороне БД)
    @Cascade(org.hibernate.annotations.CascadeType.SAVE_UPDATE)
    private List<Event> events;
    
    public User(String name) {
        this.name = name;
    }
    
    @Override
    public String toString() {
        return "User{" +
               "name='" + name + '\'' +
               '}';
    }
    /**
     
     *
     *https://www.geeksforgeeks.org/servlet-crud-operation-with-example/
     *
     *
     *
     *
     *
     *         package foobar;
     *
     * import org.flywaydb.core.Flyway;
     *
     * public class App {
     *     public static void main(String[] args) {
     *
     *         // Create the Flyway instance and point it to the database
     *         Flyway flyway =
     *                 Flyway.configure()
     *                       .dataSource( "jdbc:h2:file:./target/foobar" , "scott" , "tiger" )  // (url, user, password)
     *                       .load()                                                            // Returns a `Flyway` object.
     *         ;
     *
     *         // Start the migration
     *         flyway.migrate();
     *
     *     }
     * }
     */
}
