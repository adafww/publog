package main.model;

import javax.persistence.*;
import java.util.Date;

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
    @JoinColumn(name = "post_id", nullable = false, insertable = false, updatable = false)
    private Post postId;
    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "user_id", nullable = false, insertable = false, updatable = false)
    private User userId;
    @Column(nullable = false)
    private Date time;
    @Column(nullable = false)
    private String text;
}
