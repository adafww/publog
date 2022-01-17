package main.api.response;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class PostVotesResponse {
    private boolean result;

    public PostVotesResponse(boolean result) {
        this.result = result;
    }
}
