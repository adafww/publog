package main.api.response;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ApiStatisticsResponse {

    private int postsCount;
    private int likesCount;
    private int dislikesCount;
    private int viewsCount;
    private long firstPublication;

    public ApiStatisticsResponse(int postsCount, int likesCount, int dislikesCount, int viewsCount, long firstPublication) {
        this.postsCount = postsCount;
        this.likesCount = likesCount;
        this.dislikesCount = dislikesCount;
        this.viewsCount = viewsCount;
        this.firstPublication = firstPublication;
    }

    public ApiStatisticsResponse() {
    }
}
