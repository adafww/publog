package main.api.response;

import lombok.Getter;
import lombok.Setter;

import java.util.Hashtable;

@Getter
@Setter
public class RestoreErrorResponse extends RestoreAbsrtactResponse{

    private boolean result = false;
    private Hashtable<String, String> errors;

    public RestoreErrorResponse(Hashtable<String, String> errors) {
        this.errors = errors;
    }
}
