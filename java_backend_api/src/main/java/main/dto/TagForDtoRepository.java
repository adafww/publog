package main.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TagForDtoRepository {
    private String name;
    private long count;

    public TagForDtoRepository(String name, long count) {
        this.name = name;
        this.count = count;
    }
}
