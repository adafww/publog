package main.model;

import lombok.Getter;
import lombok.Setter;
import javax.persistence.*;
import java.util.Date;

@Getter
@Setter
@Entity
@Table(name =  "post_comments")
public class PostComment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "parent_id", insertable = false, updatable = false)
    private PostComment parentId;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "post_id", referencedColumnName = "id", updatable = false)
    private Post postId;
    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "user_id", referencedColumnName = "id", updatable = false)
    private User userId;
    @Column(nullable = false)
    private Date time;
    @Column(nullable = false)
    private String text;

    public PostComment(PostComment parentId, Post postId, User userId, Date time, String text) {
        this.parentId = parentId;
        this.postId = postId;
        this.userId = userId;
        this.time = time;
        this.text = text;
    }

    public PostComment(Post postId, User userId, Date time, String text) {
        this.postId = postId;
        this.userId = userId;
        this.time = time;
        this.text = text;
    }

    public PostComment() {

    }
}
