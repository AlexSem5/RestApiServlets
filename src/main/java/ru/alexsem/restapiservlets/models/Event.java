package ru.alexsem.restapiservlets.models;

import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

/**
 * Entities:
 * User -> Integer id, String name, List<Event> events
 * Event -> Integer id, User user, File file
 * File -> Integer id, String name, String filePath, Event event
 *
 * Many-to-one relationship: Events-User
 * One-to-one relationship: Event-File
 */
@Entity
@Table(name = "Event")
@Getter @Setter
@NoArgsConstructor
public class Event {
    @Id
    @Column(name = "id")
//    id генерируется на стороне бд
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    
    // Эта сущность является owning side - владеющая сторона (foreign key)
// Названия колонок в дочерн и родит таблицах
    @ManyToOne
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private User user;
    
    @OneToOne
    @JoinColumn(name = "file_id", referencedColumnName = "id")
    private File file;
    
    public Event(User user, File file) {
        this.user = user;
        this.file = file;
    }
    /**
     * Попробовать метод load()
     * Возможно создать метод в модели (см Cascade)
     */
}
