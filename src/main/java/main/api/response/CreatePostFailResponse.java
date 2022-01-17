package main.api.response;

import lombok.Getter;
import lombok.Setter;

import java.util.Hashtable;
@Getter
@Setter
public class CreatePostFailResponse extends CreatePostAbstractResponse{

    private boolean result = false;
    private Hashtable<String, String> errors;

    public CreatePostFailResponse(Hashtable<String, String> errors) {
        this.errors = errors;
    }

}
