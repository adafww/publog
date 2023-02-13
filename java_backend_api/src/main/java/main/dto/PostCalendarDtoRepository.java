package main.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
public class PostCalendarDtoRepository {

    private Date time;
    private long count;

    public PostCalendarDtoRepository(Date time, long count) {

        this.time = time;
        this.count = count;

    }
}
