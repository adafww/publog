package main;

import main.api.request.CommentRequest;
import main.repository.PostCommentRepository;
import main.repository.UserRepository;
import main.service.CommentService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import java.util.Date;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("Тестирование CommentService")
public class CommentServiceTest {

    @Mock
    private PostCommentRepository postCommentRepo;

    @Mock
    private UserRepository userRepo;

    @Test
    @DisplayName("Коментарий успешно сохранен")
    void getCommentOkTest(){

        CommentRequest request = new CommentRequest();
        request.setPostId(1);
        request.setText("test");
        String userEmail = "userEmail";

        when(userRepo.idByEmail(userEmail)).thenReturn(1);

        new CommentService(postCommentRepo, userRepo).getCommentOk(request, userEmail);

        verify(postCommentRepo, times(1)).saveCommentPost(
                anyInt(),
                anyInt(),
                any(Date.class),
                anyString());
    }

    @Test
    @DisplayName("Ответ на коментарий успешно сохранен")
    void getCommentOkWithParentIdTest(){

        CommentRequest request = new CommentRequest();
        request.setParentId("1");
        request.setPostId(1);
        request.setText("test");
        String userEmail = "userEmail";

        when(userRepo.idByEmail(userEmail)).thenReturn(1);

        new CommentService(postCommentRepo, userRepo).getCommentOk(request, userEmail);

        verify(postCommentRepo, times(1)).saveParentCommentPost(
                anyInt(),
                anyInt(),
                anyInt(),
                any(Date.class),
                anyString());
    }
}
