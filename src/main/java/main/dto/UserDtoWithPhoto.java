package main.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserDtoWithPhoto {

    private int id;
    private String name;
    private String photo;

    public UserDtoWithPhoto(int id, String name, String photo) {

        this.id = id;
        this.name = name;
        this.photo = photo;

    }
}
