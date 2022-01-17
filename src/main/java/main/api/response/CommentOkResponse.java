package main.api.response;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CommentOkResponse extends CommentAbstractResponse{
    private int id;

    public CommentOkResponse(int id) {
        this.id = id;
    }
}
