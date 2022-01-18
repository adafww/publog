package main.model;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Check;
import javax.persistence.*;
import java.util.Date;

@Getter
@Setter
@Entity
@Table(name = "posts")
public class Post {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @Column(name = "is_active", nullable = false)
    private boolean isActive;
    @Enumerated(EnumType.STRING)
    @Column(name = "moderation_status", nullable = false, columnDefinition = "enum('NEW', 'ACCEPTED', 'DECLINED')")
    private ModerationStatusType moderationStatusType;
    @ManyToOne(cascade = CascadeType.ALL)
    @Check(constraints = "moderator_id.isModerator == true OR NULL")
    @JoinColumn(name = "moderator_id", insertable = false, updatable = false)
    private User Moderator;
    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name="user_id",  referencedColumnName = "id", nullable=false)
    private User user;
    @Column(nullable = false)
    private Date time;
    @Column(columnDefinition = "TEXT", nullable = false)
    private String title;
    @Column(columnDefinition = "TEXT", nullable = false)
    private String text;
    @Column(name = "view_count", nullable = false)
    private int viewCount;

    public Post(boolean isActive, ModerationStatusType moderationStatusType, User user, Date time, String title, String text) {
        this.isActive = isActive;
        this.moderationStatusType = moderationStatusType;
        this.user = user;
        this.time = time;
        this.title = title;
        this.text = text;
    }

    public Post() {

    }

    public Post(int id) {
        this.id = id;
    }
}
