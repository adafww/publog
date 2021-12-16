package main.model;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import javax.persistence.*;

@Getter
@Setter
@Entity
@Table(name =  "tag2post")
public class Tag2Post {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @OneToOne
    @JoinColumn(name = "post_id", insertable = false, updatable = false, nullable = false)
    private Post postId;
    @ManyToOne
    @JoinColumn(name = "tag_id", insertable = false, updatable = false, nullable = false)
    private Tag tagID;
}
