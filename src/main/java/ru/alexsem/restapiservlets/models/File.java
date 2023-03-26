package ru.alexsem.restapiservlets.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.Cascade;

import javax.persistence.*;

/**
 * Entities:
 * User -> Integer id, String name, List<Event> events
 * Event -> Integer id, User user, File file
 * File -> Integer id, String name, String filePath, Event event
 *
 */

@Entity
@Table(name = "File")
@Getter @Setter
@NoArgsConstructor
public class File {
    @Id
    @Column(name = "id")
//    id генерируется на стороне бд
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    
    @Column(name = "name")
    private String name;
    
    @Column(name = "filePath")
    private String filePath;
    
    //  Название поля с аннотацией @JoinColumn в доч таблице:
    //  (cсылаемся на поле owning side)
    @OneToOne(mappedBy = "file", fetch = FetchType.LAZY)
    @Cascade(org.hibernate.annotations.CascadeType.SAVE_UPDATE)
    @JsonIgnore
    private Event event;
    
    public File(String name, String filePath) {
        this.name = name;
        this.filePath = filePath;
    }
}
