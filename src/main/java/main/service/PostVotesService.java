package main.service;

import lombok.RequiredArgsConstructor;
import main.api.response.PostVotesResponse;
import main.model.Post;
import main.model.PostVote;
import main.model.User;
import main.repository.PostRepository;
import main.repository.PostVoteRepository;
import main.repository.UserRepository;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.Date;

@RequiredArgsConstructor
@Service
public class PostVotesService {

    private final PostVoteRepository postVoteRepo;
    private final UserRepository userRepo;

    public PostVotesResponse getLike(int postId){

        int userId = userRepo.idByEmail(SecurityContextHolder.getContext().getAuthentication().getName());

        if(postVoteRepo.voted(postId, userId)){

            if(postVoteRepo.isLike(postId, userId)){

                postVoteRepo.deleteByUserIdAndPostId(postId, userId);
            }else {

                postVoteRepo.update(postId, userId, true);
            }

            return new PostVotesResponse(false);
        }else {

            postVoteRepo.insertInto(new Date(), postId, userId, true);

            return new PostVotesResponse(true);
        }
    }

    public PostVotesResponse getDislike(int postId){

        int userId = userRepo.idByEmail(SecurityContextHolder.getContext().getAuthentication().getName());

        if(postVoteRepo.voted(postId, userId)){

            if(!postVoteRepo.isLike(postId, userId)){

                postVoteRepo.deleteByUserIdAndPostId(postId, userId);
            }else {

                postVoteRepo.update(postId, userId, false);
            }

            return new PostVotesResponse(false);
        }else {

            postVoteRepo.insertInto(new Date(), postId, userId, false);

            return new PostVotesResponse(true);
        }
    }
}
