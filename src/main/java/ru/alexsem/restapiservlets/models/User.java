package ru.alexsem.restapiservlets.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;
import org.hibernate.annotations.Cascade;

import javax.persistence.*;
import java.util.ArrayList;
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
    
    /**
     * The mapping definition consists of 2 parts:
     * the to-many side of the association which owns the relationship mapping and
     * the to-one side which just references the mapping
     */
//  Lazy - связанные товары загрузятся только по запросу (вызов геттера)
//  Ссылаемся на поле owning side с аннотацией @JoinColumn в доч таблице
    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY)
    //Если сохраняем человека, то автоматически сохраняется связанная сущность
//    Можем добавить каскадир других операций (on delete уже настроено на стороне БД)
    @Cascade(org.hibernate.annotations.CascadeType.SAVE_UPDATE)
    @JsonIgnore
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
    
    public void addEvent(Event event) {
        if (this.events == null) {
            this.events = new ArrayList<>();
        }
        this.events.add(event);
        event.setUser(this);
    }
}
