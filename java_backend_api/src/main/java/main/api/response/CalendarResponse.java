package main.api.response;

import lombok.Getter;
import lombok.Setter;
import java.util.HashSet;
import java.util.Hashtable;

@Getter
@Setter
public class CalendarResponse {

    private HashSet<String> years;
    private Hashtable<String, Integer> posts;

}
