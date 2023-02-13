package main.service;

import lombok.RequiredArgsConstructor;
import main.api.response.ErrorResponse;
import main.repository.PostVoteRepository;
import main.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.Date;

@RequiredArgsConstructor
@Service
public class PostVotesService {

    private final PostVoteRepository postVoteRepo;
    private final UserRepository userRepo;

    public ErrorResponse getLike(int postId, String userEmail){

        int userId = userRepo.idByEmail(userEmail);

        if(postVoteRepo.voted(postId, userId)){

            if(postVoteRepo.isLike(postId, userId)){

                postVoteRepo.deleteByUserIdAndPostId(postId, userId);
            }else {

                postVoteRepo.update(postId, userId, true);
            }

            return new ErrorResponse(false);
        }else {

            postVoteRepo.insertInto(new Date(), postId, userId, true);

            return new ErrorResponse(true);
        }
    }

    public ErrorResponse getDislike(int postId, String userEmail){

        int userId = userRepo.idByEmail(userEmail);

        if(postVoteRepo.voted(postId, userId)){

            if(!postVoteRepo.isLike(postId, userId)){

                postVoteRepo.deleteByUserIdAndPostId(postId, userId);
            }else {

                postVoteRepo.update(postId, userId, false);
            }

            return new ErrorResponse(false);
        }else {

            postVoteRepo.insertInto(new Date(), postId, userId, false);

            return new ErrorResponse(true);
        }
    }
}
