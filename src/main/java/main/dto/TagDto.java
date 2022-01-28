package main.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TagDto {
    private String name;
    private double weight;

    public TagDto(String name, double weight) {
        this.name = name;
        this.weight = weight;
    }

    public TagDto() {

    }
}
