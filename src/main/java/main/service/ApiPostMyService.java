package main.service;

import lombok.RequiredArgsConstructor;
import main.api.response.ApiPostMyResponse;
import main.dto.PostForDtoRepository;
import main.dto.PostMyDto;
import main.repository.PostRepository;
import main.repository.UserRepository;
import org.jsoup.Jsoup;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
@Service
public class ApiPostMyService {

    private final PostRepository postRepo;
    private final UserRepository userRepo;

    public ApiPostMyResponse getApiPostMyResponse(int offset, int limit, String email, String status){

        List<PostForDtoRepository> postForDtoRepositoryList = null;

        if(status.equals("inactive")){
            postForDtoRepositoryList = postRepo.findInactiveByEmail(email, PageRequest.of(offset, limit));
        }else if(status.equals("pending")){
            postForDtoRepositoryList = postRepo.findPendingByEmail(email, PageRequest.of(offset, limit));
        }else if(status.equals("declined")){
            postForDtoRepositoryList = postRepo.findDeclinedByEmail(email, PageRequest.of(offset, limit));
        }else if(status.equals("published")) {
            postForDtoRepositoryList = postRepo.findPublishedByEmail(email, PageRequest.of(offset, limit));
        }

        ApiPostMyResponse apiPostMyResponse = new ApiPostMyResponse();
        apiPostMyResponse.setCount(postForDtoRepositoryList.size());
        apiPostMyResponse.setPosts(postMyDtoList(postForDtoRepositoryList));

        return apiPostMyResponse;
    }

    public List<PostMyDto> postMyDtoList(List<PostForDtoRepository> listDto){

        List<PostMyDto> dtoFinal = new ArrayList<>();
        PostMyDto dto;
        int announceLimit = 150;
        for (PostForDtoRepository post : listDto){

            dto = new PostMyDto();
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
