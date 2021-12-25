package main.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CommentPostDto {

    private int id;
    private long timestamp;
    private String text;
    private UserDtoWithPhoto user;

}
