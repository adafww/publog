package main;

import main.api.response.StatisticsResponse;
import main.dto.StatisticsDto;
import main.repository.PostRepository;
import main.repository.PostVoteRepository;
import main.service.StatisticsService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@ExtendWith(MockitoExtension.class)
@DisplayName("Тестирование StatisticsService")
public class StatisticsTest {

    @Mock
    private PostRepository postRepo;

    @Mock
    private PostVoteRepository postVoteRepo;

    @Test
    @DisplayName("Запрос Общей статистики")
    void getAllTest(){

        Date date = new Date();
        Mockito.when(postRepo.existsBy()).thenReturn(true);
        Mockito.when(postRepo.getStatistics()).thenReturn(new StatisticsDto(1, 2, 1, 5, date));

        StatisticsResponse dto = new StatisticsService(postRepo, postVoteRepo).getAll();

        Assertions.assertTrue(dto.getPostsCount() == 1 &&
                dto.getLikesCount() == 2 &&
                dto.getDislikesCount() == 1 &&
                dto.getViewsCount() == 5 &&
                dto.getFirstPublication() == Long.parseLong(Long.toString(date.getTime())
                        .substring(0, Long.toString(date.getTime()).length() - 3)));

    }

    @Test
    @DisplayName("Запрос статистики пользователя")
    void getUserTest(){

        Date date = new Date();
        String name = "userEmail";

        Mockito.when(postRepo.existsByUser(name)).thenReturn(true);
        Mockito.when(postVoteRepo.userExists(name)).thenReturn(true);
        Mockito.when(postRepo.getUserStatistics(name)).thenReturn(new StatisticsDto(1, 1, 2, 5, date));

        StatisticsResponse dto = new StatisticsService(postRepo, postVoteRepo).getMy(name);

        Assertions.assertTrue(dto.getPostsCount() == 1 &&
                dto.getLikesCount() == 1 &&
                dto.getDislikesCount() == 2 &&
                dto.getViewsCount() == 5 &&
                dto.getFirstPublication() == Long.parseLong(Long.toString(date.getTime())
                        .substring(0, Long.toString(date.getTime()).length() - 3)));
    }

    @Test
    @DisplayName("Запрос статистики пользователя без созданных пользователем постов")
    void getUserStaticsExeptPostsTest(){

        String name = "userEmail";
        List<Byte> votesList = new ArrayList<>();

        votesList.add((byte) 1);
        votesList.add((byte) 1);
        votesList.add((byte) 0);

        Mockito.when(postRepo.existsByUser(name)).thenReturn(false);
        Mockito.when(postVoteRepo.userExists(name)).thenReturn(true);
        Mockito.when(postVoteRepo.getUserVotes(name)).thenReturn(votesList);

        StatisticsResponse dto = new StatisticsService(postRepo, postVoteRepo).getMy(name);

        Assertions.assertTrue(dto.getPostsCount() == 0 &&
                dto.getLikesCount() == 2 &&
                dto.getDislikesCount() == 1 &&
                dto.getViewsCount() == 0 &&
                dto.getFirstPublication() == 0);

    }
}
