package main.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;
@Getter
@Setter
public class PostForDtoRepository {
    private int id;
    private Date timestamp;
    private int userId;
    private String title;
    private String announce;
    private long likeCount;
    private long dislikeCount;
    private long commentCount;
    private int viewCount;

    public PostForDtoRepository(int id, Date timestamp, int userId, String title, String announce, long likeCount, long dislikeCount, long commentCount, int viewCount) {
        this.id = id;
        this.timestamp = timestamp;
        this.userId = userId;
        this.title = title;
        this.announce = announce;
        this.likeCount = likeCount;
        this.dislikeCount = dislikeCount;
        this.commentCount = commentCount;
        this.viewCount = viewCount;
    }
    public PostForDtoRepository(){}
}

