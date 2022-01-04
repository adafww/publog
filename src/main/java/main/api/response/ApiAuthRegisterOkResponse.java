package main.api.response;

import lombok.Getter;

@Getter
public class ApiAuthRegisterOkResponse extends ApiAuthRegisterAbstractResponse {

    private boolean result = true;

    public ApiAuthRegisterOkResponse() {
    }
}
