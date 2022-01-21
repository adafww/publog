package main.api.response;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RestoreResponse extends RestoreAbsrtactResponse{
    private boolean result;

    public RestoreResponse(boolean result) {
        this.result = result;
    }
}
