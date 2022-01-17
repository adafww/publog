package main.api.response;

import lombok.Getter;

@Getter
public class CreatePostFalseResponse extends CreatePostAbstractResponse{
    private boolean result = false;
}
