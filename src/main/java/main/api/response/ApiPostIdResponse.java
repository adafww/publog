package main.api.response;

import lombok.Getter;
import lombok.Setter;
import main.dto.CommentForPostForDto;
import main.dto.CommentPostDto;
import main.dto.UserDto;

import java.util.HashSet;
import java.util.List;

@Getter
@Setter
public class ApiPostIdResponse {

    private int id;
    private long timestamp;
    private UserDto user;
    private String title;
    private String text;
    private long likeCount;
    private long dislikeCount;
    private long commentCount;
    private int viewCount;
    private List<CommentPostDto> comments;
    private HashSet<String> tags;

}
