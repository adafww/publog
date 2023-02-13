package main.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserDto {
    private int id;
    private String name;

    public UserDto(int id, String name) {
        this.id = id;
        this.name = name;
    }
}
