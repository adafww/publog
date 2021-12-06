package main.model;

import org.hibernate.annotations.Check;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "posts")
public class Post {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @Column(name = "is_active", nullable = false)
    private boolean isActive;
    @Column(name = "moderation_status", nullable = false)
    private String moderationStatus;
    @ManyToOne(cascade = CascadeType.ALL)
    @Check(constraints = "moderator_id.isModerator == true OR NULL")
    @JoinColumn(name = "moderator_id", insertable = false, updatable = false)
    private User Moderator;
    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "user_id", nullable = false, insertable = false, updatable = false)
    private User user;
    @Column(nullable = false)
    private Date time;
    @Column(nullable = false)
    private String title;
    @Column(nullable = false)
    private String text;
    @Column(name = "view_count", nullable = false)
    private int viewCount;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        isActive = active;
    }

    public ModerationStatusType getType() {
        return ModerationStatusType.valueOf(moderationStatus);
    }

    public void setType(ModerationStatusType moderationStatus) {
        this.moderationStatus = moderationStatus.toString();
    }

    public User getModerator() {
        return Moderator;
    }

    public void setModerator(User moderator) {
        Moderator = moderator;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Date getTime() {
        return time;
    }

    public void setTime(Date time) {
        this.time = time;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public int getViewCount() {
        return viewCount;
    }

    public void setViewCount(int viewCount) {
        this.viewCount = viewCount;
    }
}
