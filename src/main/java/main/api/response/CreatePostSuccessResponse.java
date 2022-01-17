package main.api.response;

import lombok.Getter;

@Getter
public class CreatePostSuccessResponse extends CreatePostAbstractResponse{
    private boolean result = true;

    public CreatePostSuccessResponse(){}
}
