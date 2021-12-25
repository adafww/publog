package main.service;

import lombok.RequiredArgsConstructor;
import main.api.response.ApiPostIdResponse;
import main.api.response.ApiPostResponse;
import main.dto.CommentForPostForDto;
import main.dto.CommentPostDto;
import main.dto.PostDto;
import main.dto.PostForDtoRepository;
import main.repository.PostCommentRepository;
import main.repository.PostRepository;
import main.repository.UserRepository;
import org.jsoup.Jsoup;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

@RequiredArgsConstructor
@Service
public class ApiPostService {

    private final PostRepository postRepo;
    private final UserRepository userRepo;
    private final PostCommentRepository postCommentRepo;

    public ApiPostResponse getApiPostSearch(int offset, int limit, String query){

        List<PostForDtoRepository> postDtoIterable = postRepo.findPostSearch("%" + query + "%", PageRequest.of(offset, limit));
        ApiPostResponse apiPostResponse = new ApiPostResponse();
        apiPostResponse.setCount(postDtoIterable.size());
        apiPostResponse.setPosts(postDtoList(postDtoIterable));

        return apiPostResponse;
    }

    public ApiPostIdResponse getPostById(int id){

        ApiPostIdResponse apiPostIdResponse = new ApiPostIdResponse();
        PostForDtoRepository postDtoIterable = postRepo.findPostId(id).stream().findFirst().get();
        List<CommentForPostForDto> comPostForDto = postCommentRepo.findPostCommentById(id);
        List<CommentPostDto> commentPostDtoList = new ArrayList<>();
        HashSet<String> stringHashSet = new HashSet<>();
        CommentPostDto commentPostDto;
        for(CommentForPostForDto postComment : comPostForDto){
            commentPostDto = new CommentPostDto();
            commentPostDto.setId(postComment.getId());
            commentPostDto.setTimestamp(Long.parseLong(Long.toString(postComment.getTimestamp().getTime()).substring(0, Long.toString(postComment.getTimestamp().getTime()).length() - 3)));
            commentPostDto.setText(Jsoup.parse(postComment.getText()).text());
            commentPostDto.setUser(userRepo.findByIdWithPhoto(postComment.getUserId()).stream().findFirst().get());
            commentPostDtoList.add(commentPostDto);
            stringHashSet.add(postComment.getTag());
        }
        apiPostIdResponse.setId(postDtoIterable.getId());
        apiPostIdResponse.setTimestamp(Long.parseLong(Long.toString(postDtoIterable.getTimestamp().getTime()).substring(0, Long.toString(postDtoIterable.getTimestamp().getTime()).length() - 3)));
        apiPostIdResponse.setUser(userRepo.findById(postDtoIterable.getUserId()).get(0));
        apiPostIdResponse.setTitle(postDtoIterable.getTitle());
        apiPostIdResponse.setText(Jsoup.parse(postDtoIterable.getAnnounce()).text());
        apiPostIdResponse.setLikeCount(postDtoIterable.getLikeCount());
        apiPostIdResponse.setDislikeCount(postDtoIterable.getDislikeCount());
        apiPostIdResponse.setCommentCount(postDtoIterable.getCommentCount());
        apiPostIdResponse.setViewCount(postDtoIterable.getViewCount());
        apiPostIdResponse.setComments(commentPostDtoList);
        apiPostIdResponse.setTags(stringHashSet);

        return apiPostIdResponse;
    }

    public ApiPostResponse getPostByTag(int offset, int limit, String tag){

        List<PostForDtoRepository> postDtoIterable = postRepo.findByTag(tag, PageRequest.of(offset, limit));

        ApiPostResponse apiPostResponse = new ApiPostResponse();
        apiPostResponse.setCount(postDtoIterable.size());
        apiPostResponse.setPosts(postDtoList(postDtoIterable));

        return apiPostResponse;
    }

    public ApiPostResponse getPostByDate(int offset, int limit, String date){

        List<PostForDtoRepository> postDtoIterable = postRepo.findByDate("%" + date + "%", PageRequest.of(offset, limit));

        ApiPostResponse apiPostResponse = new ApiPostResponse();
        apiPostResponse.setCount(postDtoIterable.size());
        apiPostResponse.setPosts(postDtoList(postDtoIterable));

        return apiPostResponse;
    }

    public ApiPostResponse getApiPostResponse(int offset, int limit, String mode) {

        List<PostForDtoRepository> postDtoIterable;

        if(mode.equals("popular")){
            postDtoIterable = postRepo.findPopular(PageRequest.of(offset, limit));
        }else if(mode.equals("best")){
            postDtoIterable = postRepo.findBest(PageRequest.of(offset, limit));
        }else if(mode.equals("early")){
            postDtoIterable = postRepo.findEarly(PageRequest.of(offset, limit));
        }else {
            postDtoIterable = postRepo.findRecent(PageRequest.of(offset, limit));
        }

        ApiPostResponse apiPostResponse = new ApiPostResponse();
        apiPostResponse.setCount(postDtoIterable.size());
        apiPostResponse.setPosts(postDtoList(postDtoIterable));

        return apiPostResponse;
    }

    public List<PostDto> postDtoList(List<PostForDtoRepository> listDto){

        List<PostDto> dtoFinal = new ArrayList<>();
        PostDto dto;
        int announceLimit = 150;
        for (PostForDtoRepository post : listDto){

            dto = new PostDto();
            String announceText = Jsoup.parse(post.getAnnounce()).text();

            dto.setId(post.getId());
            dto.setTimestamp(Long.parseLong(Long.toString(post.getTimestamp().getTime()).substring(0, Long.toString(post.getTimestamp().getTime()).length() - 3)));
            dto.setUser(userRepo.findById(post.getUserId()).get(0));
            dto.setTitle(post.getTitle());
            dto.setAnnounce(announceText.length() > announceLimit ? announceText.substring(0, announceLimit) : announceText);
            dto.setLikeCount(post.getLikeCount());
            dto.setDislikeCount(post.getDislikeCount());
            dto.setCommentCount(post.getCommentCount());
            dto.setViewCount(post.getViewCount());

            dtoFinal.add(dto);
        }
        return dtoFinal;
    }
}
