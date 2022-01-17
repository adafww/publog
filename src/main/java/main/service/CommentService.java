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

        User user = userRepo.findByName(SecurityContextHolder.getContext().getAuthentication().getName());
        int id = -1;

        if(request.getParentId() == null){

            id = postCommentRepo.save(new PostComment(
                    postCommentRepo.findPostById(request.getPostId()),
                    user,
                    new Date(),
                    request.getText())
            ).getId();

        }else {

            id = postCommentRepo.save(new PostComment(
                    postCommentRepo.findById(Integer.parseInt(request.getParentId())),
                    postCommentRepo.findPostById(request.getPostId()),
                    user,
                    new Date(),
                    request.getText())
            ).getId();

        }

        return new CommentOkResponse(id);
    }

    public CommentAbstractResponse getCommentFalse(){

        Hashtable<String, String> hashtable = new Hashtable<>();
        hashtable.put("text", "Текст комментария не задан или слишком короткий");
        return new CommentFalseResponse(hashtable);

    }
}
