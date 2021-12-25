package main.api.response;

import lombok.Getter;
import lombok.Setter;
import main.dto.PostCalendarDtoRepository;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.TreeMap;

@Getter
@Setter
public class ApiCalendarResponse {

    private HashSet<String> years;
    private Hashtable<String, Integer> posts;

}
