package main.service;

import lombok.RequiredArgsConstructor;
import main.api.response.ApiStatisticsResponse;
import main.dto.ApiStatisticsDto;
import main.repository.Tag2PostRepository;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class ApiStatisticsService {

    private final Tag2PostRepository tag2PostRepo;

    public ApiStatisticsResponse getAll(){

        ApiStatisticsDto apiStatisticsDto = tag2PostRepo.getAllStatistics();
        System.out.println(
                apiStatisticsDto.getDislikesCount() + " - " +
                        apiStatisticsDto.getLikesCount() + " - " +
                        apiStatisticsDto.getPostsCount() + " - " +
                        apiStatisticsDto.getViewsCount() + " - " + apiStatisticsDto.getFirstPublication());

        return null;
    }
}
