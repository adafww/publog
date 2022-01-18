package main.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
public class ApiStatisticsDto {

    private long postsCount;
    private long likesCount;
    private long dislikesCount;
    private long viewsCount;
    private Date firstPublication;

    public ApiStatisticsDto(long postsCount, long likesCount, long dislikesCount, long viewsCount, Date firstPublication) {
        this.postsCount = postsCount;
        this.likesCount = likesCount;
        this.dislikesCount = dislikesCount;
        this.viewsCount = viewsCount;
        this.firstPublication = firstPublication;
    }
}
