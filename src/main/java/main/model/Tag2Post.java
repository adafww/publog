package main.model;

import javax.persistence.*;

@Entity
@Table(name =  "tag2post")
public class Tag2Post {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @OneToOne
    @JoinColumn(name = "post_id", insertable = false, updatable = false, nullable = false)
    private Post postId;
    @OneToOne
    @JoinColumn(name = "user_id", insertable = false, updatable = false, nullable = false)
    private User userID;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Post getPostId() {
        return postId;
    }

    public void setPostId(Post postId) {
        this.postId = postId;
    }

    public User getUserID() {
        return userID;
    }

    public void setUserID(User userID) {
        this.userID = userID;
    }
}
