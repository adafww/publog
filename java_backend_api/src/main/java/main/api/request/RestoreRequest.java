package main.api.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

@Getter
@Setter
public class RestoreRequest {

    private String email;

}
