package main.api.response;

import lombok.Getter;
import lombok.Setter;

import java.util.Hashtable;

@Getter
@Setter
public class CommentFalseResponse extends CommentAbstractResponse{
    private boolean result = false;
    private Hashtable<String, String> errors;

    public CommentFalseResponse(Hashtable<String, String> errors) {
        this.errors = errors;
    }
}
