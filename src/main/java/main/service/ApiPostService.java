package main.service;

import lombok.RequiredArgsConstructor;
import main.api.response.ApiPostResponse;
import main.dto.PostDto;
import main.dto.PostForDtoRepository;
import main.repository.PostRepository;
import main.repository.UserRepository;
import org.jsoup.Jsoup;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
@Service
public class ApiPostService {

    private final PostRepository postRepo;
    private final UserRepository userRepo;

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
