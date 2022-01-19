package main.service;

import lombok.RequiredArgsConstructor;
import main.api.response.ApiStatisticsResponse;
import main.dto.ApiStatisticsDto;
import main.repository.PostRepository;
import main.repository.Tag2PostRepository;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class ApiStatisticsService {

    private final Tag2PostRepository tag2PostRepo;

    public ApiStatisticsResponse getAll(){

        return get(tag2PostRepo.getAllStatistics());
    }

    public ApiStatisticsResponse getMy(){


        return get(tag2PostRepo.getMyStatistics(SecurityContextHolder.getContext().getAuthentication().getName()));
    }

    private ApiStatisticsResponse get(ApiStatisticsDto apiStatisticsDto){

        return new ApiStatisticsResponse(
                apiStatisticsDto.getPostsCount(),
                apiStatisticsDto.getLikesCount(),
                apiStatisticsDto.getDislikesCount(),
                apiStatisticsDto.getViewsCount(),
                Long.parseLong(
                        Long.toString(
                                apiStatisticsDto.getFirstPublication().getTime()).substring(
                                0, Long.toString(apiStatisticsDto.getFirstPublication().getTime()).length() - 3))
        );
    }

}
