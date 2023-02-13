package main;

import main.api.request.ModerationRequest;
import main.api.request.PostRequest;
import main.api.response.ErrorResponse;
import main.api.response.PostResponse;
import main.dto.PostForDtoRepository;
import main.dto.UserDto;
import main.model.Post;
import main.model.Tag;
import main.model.Tag2Post;
import main.model.User;
import main.model.enums.ModerationStatusType;
import main.repository.*;
import main.service.PostService;
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
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

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

    @Mock
    private GlobalSettingsRepository globalSettingsRepo;

    @Test
    @DisplayName("Тестирование метода moderationPosts")
    void moderationPostsTest(){

        ModerationRequest request = new ModerationRequest();
        String email = "userEmail";
        request.setPostId(1);
        request.setDecision("accepted");

        Assertions.assertTrue(new PostService(postRepo,userRepo, postCommentRepo, tag2PostRepo, tagRepo, globalSettingsRepo)
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

        PostResponse postResponse = new PostService(postRepo, userRepo, postCommentRepo, tag2PostRepo, tagRepo, globalSettingsRepo)
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

        PostResponse postResponse = new PostService(postRepo, userRepo, postCommentRepo, tag2PostRepo, tagRepo, globalSettingsRepo)
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

        PostResponse postResponse = new PostService(postRepo, userRepo, postCommentRepo, tag2PostRepo, tagRepo, globalSettingsRepo)
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

        PostResponse postResponse = new PostService(postRepo, userRepo, postCommentRepo, tag2PostRepo, tagRepo, globalSettingsRepo)
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

        PostResponse postResponse = new PostService(postRepo, userRepo, postCommentRepo, tag2PostRepo, tagRepo, globalSettingsRepo)
                .getApiPostSearch(0, 10, query);


        Assertions.assertTrue(postResponse.getPosts().size() == 1 && postResponse.getCount() == 1);

    }

    @Test
    @DisplayName("Тестирование метода getPostByTag")
    void getPostByTagTest() {

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

        list.add(postForDtoRepository);
        userList.add(new UserDto(1, "testName"));

        Mockito.when(postRepo.findByTag("test", PageRequest.of(0, 10))).thenReturn(list);
        Mockito.when(userRepo.findById(1)).thenReturn(userList);

        PostResponse postResponse = new PostService(postRepo, userRepo, postCommentRepo, tag2PostRepo, tagRepo, globalSettingsRepo)
                .getPostByTag(0, 10, "test");


        Assertions.assertTrue(postResponse.getPosts().size() == 1 && postResponse.getCount() == 1);

    }

    @Test
    @DisplayName("Тестирование метода getPostByDate")
    void getPostByDateTest() {

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

        list.add(postForDtoRepository);
        userList.add(new UserDto(1, "testName"));

        Mockito.when(postRepo.findByDate("%" + "date" + "%", PageRequest.of(0, 10))).thenReturn(list);
        Mockito.when(userRepo.findById(1)).thenReturn(userList);

        PostResponse postResponse = new PostService(postRepo, userRepo, postCommentRepo, tag2PostRepo, tagRepo, globalSettingsRepo)
                .getPostByDate(0, 10, "date");


        Assertions.assertTrue(postResponse.getPosts().size() == 1 && postResponse.getCount() == 1);

    }

    @Test
    @DisplayName("Тестирование метода getCreatePost")
    void getCreatePostTest() {

        String tag = "tag";
        List<String> tags = new ArrayList<>();
        tags.add(tag);
        PostRequest request = new PostRequest();
        request.setTimestamp(new Date().getTime());
        request.setActive((byte) 1);
        request.setTitle("testTitle");
        request.setTags(tags);
        request.setText("testTestTestTestTestTestTestTestTestTestTestTestTestTestTest");
        String userEmail = "userEmail";
        User user = new User(new Date(), "ТестовоеИмя", userEmail, "userPass");

        when(userRepo.findByName(userEmail)).thenReturn(user);
        when(tagRepo.existsByName(tag)).thenReturn(false);
        when(tagRepo.save(any(Tag.class))).thenReturn(new Tag(tag));

        ErrorResponse response = new PostService(postRepo, userRepo, postCommentRepo, tag2PostRepo, tagRepo, globalSettingsRepo)
                .getCreatePost(request, userEmail);

        verify(postRepo, times(1)).save(any(Post.class));
        verify(tagRepo, times(1)).save(any(Tag.class));
        verify(tag2PostRepo, times(1)).save(any(Tag2Post.class));

        Assertions.assertTrue(response.isResult());
    }

    @Test
    @DisplayName("Тестирование метода editPost")
    void editPostTest() {

        String tag = "tag";
        String tag1 = "tag1";
        List<String> oldTags = new ArrayList<>();
        oldTags.add(tag);
        List<String> newTags = new ArrayList<>();
        newTags.add(tag);
        newTags.add(tag1);
        PostRequest request = new PostRequest();
        request.setTimestamp(new Date().getTime());
        request.setActive((byte) 1);
        request.setTitle("testTitle");
        request.setTags(newTags);
        request.setText("testTestTestTestTestTestTestTestTestTestTestTestTestTestTest");
        String userEmail = "userEmail";

        when(postRepo.isAuthor(1, userEmail)).thenReturn(true);
        when(tagRepo.tagsByPostId(1)).thenReturn(oldTags);
        when(tagRepo.existsByName(tag1)).thenReturn(true);

        ErrorResponse response = new PostService(postRepo, userRepo, postCommentRepo, tag2PostRepo, tagRepo, globalSettingsRepo)
                .editPost(request, 1, 1, userEmail);

        verify(postRepo, times(1)).postUpdate(any(ModerationStatusType.class), anyInt(), any(Date.class), anyBoolean(), anyString(), anyString());
        verify(tag2PostRepo, times(1)).saveByPostAndTagName(anyInt(), anyString());

        Assertions.assertTrue(response.isResult());
        //when(tagRepo.save(any(Tag.class))).thenReturn(new Tag(tag));
        //verify(tagRepo, times(1)).save(any(Tag.class));
        //verify(tag2PostRepo, times(1)).save(any(Tag2Post.class));

    }
}