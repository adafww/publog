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
    @ManyToOne
    @JoinColumn(name = "tag_id", insertable = false, updatable = false, nullable = false)
    private Tag tagID;

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

    public Tag getTagID() {
        return tagID;
    }

    public void setTagID(Tag tagID) {
        this.tagID = tagID;
    }
}
