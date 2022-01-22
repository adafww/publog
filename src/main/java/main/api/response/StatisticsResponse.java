package main.api.response;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class StatisticsResponse {

    private long postsCount;
    private long likesCount;
    private long dislikesCount;
    private long viewsCount;
    private long firstPublication;

    public StatisticsResponse(long postsCount, long likesCount, long dislikesCount, long viewsCount, long firstPublication) {
        this.postsCount = postsCount;
        this.likesCount = likesCount;
        this.dislikesCount = dislikesCount;
        this.viewsCount = viewsCount;
        this.firstPublication = firstPublication;
    }
}
