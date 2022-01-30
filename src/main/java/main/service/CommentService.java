package main.service;

import lombok.RequiredArgsConstructor;
import main.api.request.CommentRequest;
import main.api.response.ErrorResponse;
import main.model.Post;
import main.model.PostComment;
import main.model.User;
import main.repository.PostCommentRepository;
import main.repository.UserRepository;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Hashtable;

@RequiredArgsConstructor
@Service
public class CommentService {

    private final PostCommentRepository postCommentRepo;
    private final UserRepository userRepo;

    public int getCommentOk(CommentRequest request, String userEmail){

        int userId = userRepo.idByEmail(userEmail);
        Date date = new Date();

        if(request.getParentId() == null){

            postCommentRepo.saveCommentPost(
                    request.getPostId(),
                    userId,
                    date,
                    request.getText());
        }else {

            postCommentRepo.saveParentCommentPost(
                    Integer.parseInt(request.getParentId()),
                    request.getPostId(),
                    userId,
                    date,
                    request.getText());
        }

        return postCommentRepo.id(date);
    }

    public ErrorResponse getCommentFalse(){

        Hashtable<String, String> hashtable = new Hashtable<>();
        hashtable.put("text", "Текст комментария не задан или слишком короткий");
        return new ErrorResponse(false, hashtable);

    }
}
