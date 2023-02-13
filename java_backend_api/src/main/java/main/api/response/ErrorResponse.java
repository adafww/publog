package main.api.response;

import lombok.Getter;
import lombok.Setter;

import java.util.Hashtable;

@Getter
@Setter
public class ErrorResponse {

    private boolean result = false;
    private Hashtable<String, String> errors;

    private int id;


    public ErrorResponse(boolean result, Hashtable<String, String> errors) {

        this.result = result;
        this.errors = errors;
    }

    public ErrorResponse(boolean result) {

        this.result = result;
    }

    public ErrorResponse(int id) {
        this.id = id;
    }
}
