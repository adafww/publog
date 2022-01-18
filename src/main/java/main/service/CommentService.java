package main.service;

import lombok.RequiredArgsConstructor;
import main.api.request.CommentRequest;
import main.api.response.CommentAbstractResponse;
import main.api.response.CommentFalseResponse;
import main.api.response.CommentOkResponse;
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

    public CommentAbstractResponse getCommentOk(CommentRequest request){

        int userId = userRepo.idByEmail(SecurityContextHolder.getContext().getAuthentication().getName());
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

        return new CommentOkResponse(postCommentRepo.id(date, request.getText()));
    }

    public CommentAbstractResponse getCommentFalse(){

        Hashtable<String, String> hashtable = new Hashtable<>();
        hashtable.put("text", "Текст комментария не задан или слишком короткий");
        return new CommentFalseResponse(hashtable);

    }
}
