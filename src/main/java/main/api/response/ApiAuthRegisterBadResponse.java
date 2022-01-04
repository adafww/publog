package main.api.response;

import lombok.Getter;
import lombok.Setter;

import java.util.Hashtable;

@Getter
@Setter
public class ApiAuthRegisterBadResponse extends ApiAuthRegisterAbstractResponse {

    private boolean result = false;
    private Hashtable<String, String> errors;

    public ApiAuthRegisterBadResponse(Hashtable<String, String> errors) {
        this.errors = errors;
    }
}
