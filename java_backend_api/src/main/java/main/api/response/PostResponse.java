package main.api.response;

import lombok.Getter;
import lombok.Setter;
import main.dto.PostDto;

import java.util.List;

@Setter
@Getter
public class PostResponse {

    private int count;
    private List<PostDto> posts;

}
