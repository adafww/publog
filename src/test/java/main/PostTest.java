package main;

import main.api.request.ModerationRequest;
import main.api.response.PostResponse;
import main.dto.PostDto;
import main.dto.PostForDtoRepository;
import main.dto.UserDto;
import main.model.Post;
import main.model.User;
import main.model.enums.ModerationStatusType;
import main.repository.*;
import main.service.PostService;
import org.junit.Assert;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageRequest;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@ExtendWith(MockitoExtension.class)
@DisplayName("Тестирование PostService")
public class PostTest {

    @Mock
    private PostRepository postRepo;

    @Mock
    private UserRepository userRepo;

    @Mock
    private PostCommentRepository postCommentRepo;

    @Mock
    private Tag2PostRepository tag2PostRepo;

    @Mock
    private TagRepository tagRepo;

    @Test
    @DisplayName("Тестирование метода moderationPosts")
    void moderationPostsTest(){

        ModerationRequest request = new ModerationRequest();
        String email = "userEmail";
        request.setPostId(1);
        request.setDecision("accepted");

        Assertions.assertTrue(new PostService(postRepo,userRepo, postCommentRepo, tag2PostRepo, tagRepo)
                .moderationPosts(request, email).isResult());
    }

    @Test
    @DisplayName("Тестирование метода getModerationPosts")
    void getModerationPostsTest() {

        PostForDtoRepository postForDtoRepository = new PostForDtoRepository(
                1,
                new Date(),
                1,
                "titleTest",
                "announceTest",
                1,
                1,
                1,
                1);
        List<PostForDtoRepository> list = new ArrayList<>();
        List<UserDto> userList = new ArrayList<>();
        String email = "email";

        list.add(postForDtoRepository);
        userList.add(new UserDto(1, "testName"));

        Mockito.when(postRepo.findModerationPosts(ModerationStatusType.ACCEPTED, email, PageRequest.of(0, 10))).thenReturn(list);
        Mockito.when(userRepo.findById(1)).thenReturn(userList);

        PostResponse postResponse = new PostService(postRepo, userRepo, postCommentRepo, tag2PostRepo, tagRepo)
                .getModerationPosts(0, 10, "accepted", email);


        Assertions.assertTrue(postResponse.getPosts().size() == 1 && postResponse.getCount() == 1);
    }

    @Test
    @DisplayName("Тестирование метода getApiPostMyResponse")
    void getApiPostMyResponseTest() {

        PostForDtoRepository postForDtoRepository = new PostForDtoRepository(
                1,
                new Date(),
                1,
                "titleTest",
                "announceTest",
                1,
                1,
                1,
                1);
        List<PostForDtoRepository> list = new ArrayList<>();
        List<UserDto> userList = new ArrayList<>();
        String email = "email";

        list.add(postForDtoRepository);
        userList.add(new UserDto(1, "testName"));

        Mockito.when(postRepo.findPublishedByEmail(email, PageRequest.of(0, 10))).thenReturn(list);
        Mockito.when(userRepo.findById(1)).thenReturn(userList);

        PostResponse postResponse = new PostService(postRepo, userRepo, postCommentRepo, tag2PostRepo, tagRepo)
                .getApiPostMyResponse(0, 10, email, "published");


        Assertions.assertTrue(postResponse.getPosts().size() == 1 && postResponse.getCount() == 1);

    }

    @Test
    @DisplayName("Тестирование метода getApiPostResponse")
    void getApiPostResponseTest() {

        PostForDtoRepository postForDtoRepository = new PostForDtoRepository(
                1,
                new Date(),
                1,
                "titleTest",
                "announceTest",
                1,
                1,
                1,
                1);
        List<PostForDtoRepository> list = new ArrayList<>();
        List<UserDto> userList = new ArrayList<>();
        String email = "email";

        list.add(postForDtoRepository);
        userList.add(new UserDto(1, "testName"));

        Mockito.when(postRepo.findPopular(PageRequest.of(0, 10))).thenReturn(list);
        Mockito.when(userRepo.findById(1)).thenReturn(userList);

        PostResponse postResponse = new PostService(postRepo, userRepo, postCommentRepo, tag2PostRepo, tagRepo)
                .getApiPostResponse(0, 10, "popular");


        Assertions.assertTrue(postResponse.getPosts().size() == 1 && postResponse.getCount() == 1);

    }

    @Test
    @DisplayName("Тестирование метода getApiPostSearch")
    void getApiPostSearchTest() {

        PostForDtoRepository postForDtoRepository = new PostForDtoRepository(
                1,
                new Date(),
                1,
                "titleTest",
                "announceTest",
                1,
                1,
                1,
                1);
        List<PostForDtoRepository> list = new ArrayList<>();
        List<UserDto> userList = new ArrayList<>();
        String query = "query";

        list.add(postForDtoRepository);
        userList.add(new UserDto(1, "testName"));

        Mockito.when(postRepo.findPostSearch("%" + query + "%", PageRequest.of(0, 10))).thenReturn(list);
        Mockito.when(userRepo.findById(1)).thenReturn(userList);

        PostResponse postResponse = new PostService(postRepo, userRepo, postCommentRepo, tag2PostRepo, tagRepo)
                .getApiPostSearch(0, 10, query);


        Assertions.assertTrue(postResponse.getPosts().size() == 1 && postResponse.getCount() == 1);

    }

    @Test
    @DisplayName("Тестирование метода getPostById")
    void getPostByIdTest() {

        PostForDtoRepository postForDtoRepository = new PostForDtoRepository(
                1,
                new Date(),
                1,
                "titleTest",
                "announceTest",
                1,
                1,
                1,
                1);
        List<PostForDtoRepository> list = new ArrayList<>();
        List<UserDto> userList = new ArrayList<>();
        String query = "query";

        list.add(postForDtoRepository);
        userList.add(new UserDto(1, "testName"));

        Mockito.when(postRepo.findPostSearch("%" + query + "%", PageRequest.of(0, 10))).thenReturn(list);
        Mockito.when(userRepo.findById(1)).thenReturn(userList);

        PostResponse postResponse = new PostService(postRepo, userRepo, postCommentRepo, tag2PostRepo, tagRepo)
                .getApiPostSearch(0, 10, query);


        Assertions.assertTrue(postResponse.getPosts().size() == 1 && postResponse.getCount() == 1);

    }
}


/*ModerationRequest request = new ModerationRequest();
        String email = "userEmail";
        request.setPostId(1);
        request.setDecision("accepted");

        Post post = new Post(
                true,
                ModerationStatusType.ACCEPTED,
                new User(
                        new Date(),
                        "testName",
                        "testEmail",
                        "testPassword"),
                new Date(),
                "titleTest",
                "textTest");
        List<Post> postList = new ArrayList<>();
        postList.add(post);*/
//Assertions.assertTrue(service.moderationPosts(request, email).isResult());

/*

    PostDto postDto = new PostDto(
            1,
            1L, new UserDto(
            1,
            "testName"),
            "titleTest",
            "test",
            1,
            1,
            1,
            1);*/
