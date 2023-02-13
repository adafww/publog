package main.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
public class CommentForPostForDto {

    private int id;
    private Date timestamp;
    private String text;
    private int userId;

    public CommentForPostForDto(int id, Date timestamp, String text, int userId) {
        this.id = id;
        this.timestamp = timestamp;
        this.text = text;
        this.userId = userId;
    }
}
