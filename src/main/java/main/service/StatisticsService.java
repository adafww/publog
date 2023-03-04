package main.service;

import lombok.RequiredArgsConstructor;
import main.api.response.StatisticsResponse;
import main.dto.StatisticsDto;
import main.repository.PostRepository;
import main.repository.PostVoteRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
public class StatisticsService {

    private final PostRepository postRepo;
    private final PostVoteRepository postVoteRepo;

    public StatisticsResponse getAll(){

        if (postRepo.existsBy()){
            return get(postRepo.getStatistics());
        }
        return new StatisticsResponse(0,0,0,0,0);
    }

    public StatisticsResponse getMy(String name){

        boolean postsExist = postRepo.existsByUser(name);
        boolean votesExist = postVoteRepo.userExists(name);

        if (postsExist){

            return get(postRepo.getUserStatistics(name));
        }else if(votesExist){

            List<Byte> list = postVoteRepo.getUserVotes(name);

            return new StatisticsResponse(
                    0,
                    list.stream().mapToInt(Byte::intValue).sum(),
                    list.stream().filter(a -> a == 0).count(),
                    0,
                    0
            );
        }
        return new StatisticsResponse(0,0,0,0,0);
    }

    private StatisticsResponse get(StatisticsDto dto){

        return new StatisticsResponse(
                dto.getPostsCount(),
                dto.getLikesCount(),
                dto.getDislikesCount(),
                dto.getViewsCount(),
                Long.parseLong(Long.toString(dto.getFirstPublication().getTime())
                .substring(0, Long.toString(dto.getFirstPublication().getTime()).length() - 3))
        );
    }
}
