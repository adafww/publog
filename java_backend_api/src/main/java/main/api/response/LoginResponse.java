package main.api.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class LoginResponse {
    private boolean result;
    @JsonProperty("user")
    private UserLoginResponse userLoginResponse;

    public LoginResponse(boolean result) {
        this.result = result;
    }

    public LoginResponse() {
    }
}
