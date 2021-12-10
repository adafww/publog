package main.controller;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import main.model.*;
import main.services.*;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.jsoup.Jsoup;

import java.util.ArrayList;

@RestController
public class ApiPostController {

    private final GlobalSettingsRepository settingsRepository;
    private final PostRepository postRepository;
    private final UserRepository userRepository;
    private final PostCommentRepository commentRepository;
    private final PostVoteRepository voteRepository;

    public ApiPostController(
            GlobalSettingsRepository settingsRepository,
            PostRepository postRepository,
            UserRepository userRepository,
            PostCommentRepository commentRepository, PostVoteRepository voteRepository){
        this.settingsRepository =settingsRepository;
        this.postRepository = postRepository;
        this.userRepository = userRepository;
        this.commentRepository = commentRepository;
        this.voteRepository = voteRepository;
    }

    @GetMapping("/api/init")
    public String init(){
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("title",  "DevPub(Пример)");
        jsonObject.addProperty("subtitle",  "Рассказы разработчиков");
        jsonObject.addProperty("phone",  "+7 903 666-44-55");
        jsonObject.addProperty("email",  "mail@mail.ru");
        jsonObject.addProperty("copyright",  "Дмитрий Сергеев");
        jsonObject.addProperty("copyrightFrom",  "2005");
        return jsonObject.toString();
    }
    @GetMapping("/api/settings")
    public String settings(){
        Iterable<GlobalSettings> gsIterable = settingsRepository.findAll();
        JsonObject jsonObject = new JsonObject();
        for(GlobalSettings settings : gsIterable){
            if(settings.getValue().equals("YES")){
                jsonObject.addProperty(settings.getCode(), true);
            }else if(settings.getValue().equals("NO")){
                jsonObject.addProperty(settings.getCode(), false);
            }
        }
        return jsonObject.toString();
    }

    @GetMapping("/api/auth/check")
    public String authCheck(){
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("result", false);
        return jsonObject.toString();
    }

    @GetMapping("/api/post")
    public String postInfo(){
        Iterable<Post> postIterable = postRepository.findAll();
        Iterable<User> userIterable = userRepository.findAll();
        Iterable<PostVote> postVoteIterable = voteRepository.findAll();
        Iterable<PostComment> postCommentIterable = commentRepository.findAll();
        JsonObject jsonObject = new JsonObject();
        JsonArray jsonArray = new JsonArray();
        int postCount = 0;
        jsonObject.addProperty("count", postCount);
        for(Post post : postIterable){
            postCount++;
            JsonObject postJsonObject = new JsonObject();
            JsonObject userJsonObject = new JsonObject();
            int likeCount = 0;
            int disCount = 0;
            int commentCount = 0;
            int postId = post.getId();
            int userId = post.getUser().getId();
            for(User user : userIterable){
                if(userId == user.getId()){
                    userJsonObject.addProperty(user.getName(), user.getId());
                }
            }
            for(PostVote postVote : postVoteIterable){
                if(postVote.isValue()){
                    likeCount++;
                }else {
                    disCount++;
                }
            }
            for (PostComment postComment : postCommentIterable){
                if(postId == postComment.getPostId().getId()){
                    commentCount++;
                }
            }
            postJsonObject.addProperty("id", post.getId());
            postJsonObject.addProperty("timestamp", System.currentTimeMillis());
            postJsonObject.add("user", userJsonObject);
            postJsonObject.addProperty("title", post.getTitle());
            postJsonObject.addProperty("announce", Jsoup.parse(post.getText()).text());
            postJsonObject.addProperty("likeCount", likeCount);
            postJsonObject.addProperty("disCount", disCount);
            postJsonObject.addProperty("commentCount", commentCount);
            postJsonObject.addProperty("viewCount", post.getViewCount());

            jsonArray.add(postJsonObject);
        }
        jsonObject.add("posts", jsonArray);

        return jsonObject.toString();
    }
    /*
    @GetMapping("/api/tag")
    public String apiTag(){
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("result", false);
        return jsonObject.toString();
    }
    */
}
