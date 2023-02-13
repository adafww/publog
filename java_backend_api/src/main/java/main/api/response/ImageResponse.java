package main.api.response;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;

public class ImageResponse {

    private boolean result = false;
    Hashtable<String, String> hashtable = new Hashtable<>();
    private List<Hashtable<String, String>> errors = new ArrayList<>();

    public ImageResponse(){}

    public boolean isResult() {
        return result;
    }

    public List<Hashtable<String, String>> getErrors() {
        hashtable.put("image", "Размер файла превышает допустимый размер");
        errors.add(hashtable);
        return errors;
    }
}
