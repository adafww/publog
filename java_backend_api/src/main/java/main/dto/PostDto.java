package main.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PostDto {

    private int id;
    private long timestamp;
    private UserDto user;
    private String title;
    private String announce;
    private long likeCount;
    private long dislikeCount;
    private long commentCount;
    private int viewCount;

    public PostDto() {
    }

    public PostDto(int id, long timestamp, UserDto user, String title, String announce, long likeCount, long dislikeCount, long commentCount, int viewCount) {
        this.id = id;
        this.timestamp = timestamp;
        this.user = user;
        this.title = title;
        this.announce = announce;
        this.likeCount = likeCount;
        this.dislikeCount = dislikeCount;
        this.commentCount = commentCount;
        this.viewCount = viewCount;
    }
}
