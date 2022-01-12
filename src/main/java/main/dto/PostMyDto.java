package main.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PostMyDto {

    private int id;
    private long timestamp;
    private String title;
    private String announce;
    private long likeCount;
    private long dislikeCount;
    private long commentCount;
    private int viewCount;
    private UserDto user;

}
