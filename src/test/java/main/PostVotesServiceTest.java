package main;

import main.api.response.ErrorResponse;
import main.repository.PostVoteRepository;
import main.repository.UserRepository;
import main.service.PostVotesService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import java.util.Date;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("Тестирование PostVotesService")
public class PostVotesServiceTest {

    @Mock
    private PostVoteRepository postVoteRepo;

    @Mock
    private UserRepository userRepo;

    @Test
    @DisplayName("Тестирование метода getLike")
    void getLikeTest() {

        String userEmail = "userEmail";

        when(userRepo.idByEmail(userEmail)).thenReturn(1);
        when(postVoteRepo.voted(1, 1)).thenReturn(false);

        ErrorResponse response = new PostVotesService(postVoteRepo, userRepo).getLike(1, userEmail);

        verify(postVoteRepo, times(1)).insertInto(any(Date.class), anyInt(), anyInt(), anyBoolean());
        Assertions.assertTrue(response.isResult());
    }

    @Test
    @DisplayName("Тестирование метода getDislike")
    void getDislikeTest() {

        String userEmail = "userEmail";
        when(userRepo.idByEmail(userEmail)).thenReturn(1);
        when(postVoteRepo.voted(1, 1)).thenReturn(false);

        new PostVotesService(postVoteRepo, userRepo).getDislike(1, userEmail);

        verify(postVoteRepo, times(1)).insertInto(any(Date.class), anyInt(), anyInt(), anyBoolean());

    }
}
