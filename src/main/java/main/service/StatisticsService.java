package main.service;

import lombok.RequiredArgsConstructor;
import main.api.response.StatisticsResponse;
import main.dto.ApiStatisticsDto;
import main.model.Post;
import main.repository.PostRepository;
import main.repository.PostVoteRepository;
import main.repository.Tag2PostRepository;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class StatisticsService {

    private final PostRepository postRepo;
    private final PostVoteRepository postVoteRepository;

    public StatisticsResponse getAll(){

        return get(
                postRepo.getAllStatistics(),
                postVoteRepository.allLikeCount(),
                postVoteRepository.allDislikeCount());
    }

    private StatisticsResponse get(List<Post> list, int likeCount, int dislikeCount){

        Date date = list.stream().map(Post::getTime).min(Date::compareTo).orElse(null);
        long longDate;
        int postCount;
        int viewsCount;

        if(date != null){

            longDate = Long.parseLong(Long.toString(date.getTime())
                    .substring(0, Long.toString(date.getTime()).length() - 3));
        }else {

            longDate = 0;
        }

        if(!list.isEmpty()){

            postCount = list.size();
            viewsCount = list.stream().mapToInt(Post::getViewCount).sum();
        }else {

            postCount = 0;
            viewsCount = 0;
        }

        return new StatisticsResponse(postCount, likeCount,dislikeCount, viewsCount, longDate);
    }

    public StatisticsResponse getMy(){

        String name = SecurityContextHolder.getContext().getAuthentication().getName();

        return get(postRepo.getMyStatistics(name), postVoteRepository.usersLikeCount(name), postVoteRepository.usersDislikeCount(name));
    }
}
