package main.model;

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
    @JoinColumn(name="post_id",  referencedColumnName = "id", nullable=false)
    private Post postId;
    @ManyToOne
    @JoinColumn(name = "tag_id", referencedColumnName = "id", nullable = false)
    private Tag tagID;

    public Tag2Post(Post postId, Tag tagID) {
        this.postId = postId;
        this.tagID = tagID;
    }

    public Tag2Post() {

    }
}
