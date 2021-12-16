package main.service;

import lombok.RequiredArgsConstructor;
import main.api.response.ApiPostResponse;
import main.dto.PostDto;
import main.dto.UserDto;
import main.model.Post;
import main.model.PostComment;
import main.model.PostVote;
import main.model.User;
import main.repository.PostCommentRepository;
import main.repository.PostRepository;
import main.repository.PostVoteRepository;
import main.repository.UserRepository;
import org.jsoup.Jsoup;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
@Service
public class ApiPostService {

    private final PostRepository postRepo;
    private final UserRepository userRepo;
    private final PostVoteRepository postVoteRepo;
    private final PostCommentRepository postCommentRepo;

    public ApiPostResponse getApiPostResponse(int offset, int limit, String mode){

        List<PostDto> dtoList;
        if(mode.equals("popular")){

            dtoList = postDtoList(
                    postRepo.findPopular(PageRequest.of(offset, limit)),
                    userRepo.findAll(),
                    postVoteRepo.findAll(),
                    postCommentRepo.findAll()
            );

        }else if(mode.equals("best")){

            dtoList = postDtoList(
                    postRepo.findBest(PageRequest.of(offset, limit)),
                    userRepo.findAll(),
                    postVoteRepo.findAll(),
                    postCommentRepo.findAll()
            );

        }else if(mode.equals("early")){

            dtoList = postDtoList(
                    postRepo.findRecentEarly(PageRequest.of(offset, limit, Sort.by(Sort.Direction.ASC, "time"))),
                    userRepo.findAll(),
                    postVoteRepo.findAll(),
                    postCommentRepo.findAll()
            );

        }else {

            dtoList = postDtoList(
                    postRepo.findRecentEarly(PageRequest.of(offset, limit, Sort.by(Sort.Direction.DESC, "time"))),
                    userRepo.findAll(),
                    postVoteRepo.findAll(),
                    postCommentRepo.findAll()
            );
        }
        ApiPostResponse dto = new ApiPostResponse();
        dto.setCount(dtoList.size());
        dto.setPosts(dtoList);
        return dto;
    }
    public List<PostDto> postDtoList(Iterable<Post> postRepo, Iterable<User> userRepo, Iterable<PostVote> postVoteRepo, Iterable<PostComment> postCommentRepo){

        ArrayList<PostDto> dtoList = new ArrayList<>();
        PostDto dto;
        int announceLimit = 150;

        for (Post post : postRepo){

            dto = new PostDto();

            String announceText = Jsoup.parse(post.getText()).text();

            dto.setId(post.getId());
            dto.setTimestamp(Long.parseLong(Long.toString(post.getTime().getTime()).substring(0, Long.toString(post.getTime().getTime()).length() - 3)));
            dto.setUser(findUserDtoById(userRepo, post.getUser().getId()));
            dto.setTitle(post.getTitle());
            dto.setAnnounce(announceText.length() > announceLimit ? announceText.substring(0, announceLimit) : announceText);
            dto.setLikeCount(likeCount(postVoteRepo, post.getId()));
            dto.setDislikeCount(dislikeCount(postVoteRepo, post.getId()));
            dto.setCommentCount(commentCount(postCommentRepo, post.getId()));
            dto.setViewCount(post.getViewCount());

            dtoList.add(dto);
        }
        return dtoList;
    }
    private UserDto findUserDtoById(Iterable<User> userRepo, int id){
        UserDto dto = new UserDto();
        dto.setId(id);
        for(User user : userRepo){
            if(user.getId() == id){
                dto.setName(user.getName());
            }
        }
        return dto;
    }
    private int likeCount(Iterable<PostVote> postVoteRepo, int id){
        int count = 0;
        for(PostVote postVote : postVoteRepo){
            if(postVote.getPost().getId() == id && postVote.isValue()){
                count++;
            }
        }
        return count;
    }
    private int dislikeCount(Iterable<PostVote> postVoteRepo, int id){
        int count = 0;
        for(PostVote postVote : postVoteRepo){
            if(postVote.getPost().getId() == id && !postVote.isValue()){
                count++;
            }
        }
        return count;
    }
    private int commentCount(Iterable<PostComment> postCommentRepo, int id){
        int count = 0;
        for (PostComment postComment : postCommentRepo){
            if(postComment.getPostId().getId() == id){
                count++;
            }
        }
        return count;
    }

}
