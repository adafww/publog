package main.api.response;

import lombok.Getter;
import lombok.Setter;
import main.dto.PostMyDto;

import java.util.List;

@Getter
@Setter
public class ApiPostMyResponse {

    private int count;
    private List<PostMyDto> posts;

}
