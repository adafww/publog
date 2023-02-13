package main.model;

import lombok.Getter;
import lombok.Setter;
import javax.persistence.*;

@Getter
@Setter
@Entity
@Table(name = "tags")
public class Tag {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @Column(nullable = false)
    private String name;

    public Tag(String name) {
        this.name = name;
    }

    public Tag() {

    }

    public Tag(int id, String name) {
        this.id = id;
        this.name = name;
    }
}
