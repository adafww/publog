package main.controller;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import main.model.*;
import main.services.*;
import org.apache.tomcat.util.digester.ArrayStack;
import org.jsoup.Jsoup;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.web.PageableArgumentResolver;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.sql.Timestamp;
import java.text.DecimalFormat;
import java.util.*;

@RestController
public class ApiGeneralController {

    private final GlobalSettingsRepository settingsRepository;
    private final PostRepository postRepository;
    private final UserRepository userRepository;
    private final PostCommentRepository commentRepository;
    private final PostVoteRepository voteRepository;
    private final TagRepository tagRepository;
    private final Tag2PostRepository tag2PostRepository;

    public ApiGeneralController(GlobalSettingsRepository settingsRepository,
                                PostRepository postRepository,
                                UserRepository userRepository,
                                PostCommentRepository commentRepository,
                                PostVoteRepository voteRepository,
                                TagRepository tagRepository,
                                Tag2PostRepository tag2PostRepository) {
        this.settingsRepository = settingsRepository;
        this.postRepository = postRepository;
        this.userRepository = userRepository;
        this.commentRepository = commentRepository;
        this.voteRepository = voteRepository;
        this.tagRepository = tagRepository;
        this.tag2PostRepository = tag2PostRepository;
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
    @GetMapping(value = "/api/post", params = {"offset", "limit", "mode"})
    public String postInfo(@RequestParam int offset, @RequestParam int limit, @RequestParam String mode){

        if(mode.equals("popular")){
            Iterable<Post> postIterable = postRepository.findAll();
            Iterable<PostComment> commIterable = commentRepository.findAll();
            Hashtable<Post, Integer> list = new Hashtable<>();
            ArrayList<Post> finalList = new ArrayList<>();
            for (Post post : postIterable){
                if(post.getModerationStatusType() == ModerationStatusType.ACCEPTED){
                }
                list.put(post, commentCount(post.getId(), commIterable));
            }
            list.entrySet().stream().sorted(Map.Entry.<Post, Integer>comparingByValue().reversed()).limit(limit).forEach(a -> finalList.add(a.getKey()));
            return postIterable(finalList);

        }else if(mode.equals("best")){
            Iterable<Post> postIterable = postRepository.findAll();
            Iterable<PostVote> voteIterable = voteRepository.findAll();
            Hashtable<Post, Integer> list = new Hashtable<>();
            ArrayList<Post> finalList = new ArrayList<>();
            for (Post post : postIterable){
                if((dislikeCount(post.getId(), voteIterable) > 0) && post.getModerationStatusType() == ModerationStatusType.ACCEPTED){
                    list.put(post, likeAndDislikeCount(post.getId(), voteIterable)[0]);
                }
            }
            list.entrySet().stream().sorted(Map.Entry.<Post, Integer>comparingByValue().reversed()).limit(limit).forEach(a -> finalList.add(a.getKey()));
            return postIterable(postRepository.findAll(Sort.by(Sort.Direction.ASC, "time")));

        }else if(mode.equals("early")){

            return postIterable(postRepository.findAll(PageRequest.of(offset, limit, Sort.by(Sort.Direction.ASC, "time"))));

        }else {

            return postIterable(postRepository.findAll(PageRequest.of(offset, limit, Sort.by(Sort.Direction.DESC, "time"))));

        }
    }
    private String postIterable(Iterable<Post> postIterable1){
        Iterable<Post> postIterable = postIterable1;
        Iterable<User> userIterable = userRepository.findAll();
        JsonObject jsonObject = new JsonObject();
        JsonArray jsonArray = new JsonArray();
        int postCount = 0;
        int announceLimit = 150;
        for(Post post : postIterable){
            postCount++;
            JsonObject postJsonObject = new JsonObject();
            JsonObject userJsonObject = new JsonObject();
            int postId = post.getId();
            int userId = post.getUser().getId();
            String announceText = Jsoup.parse(post.getText()).text();
            for(User user : userIterable){
                if(userId == user.getId()){
                    userJsonObject.addProperty("id", user.getId());
                    userJsonObject.addProperty("name", user.getName());
                }
            }
            postJsonObject.addProperty("id", post.getId());
            postJsonObject.addProperty("timestamp", Long.parseLong(Long.toString(post.getTime().getTime()).substring(0, Long.toString(post.getTime().getTime()).length() - 3)));
            postJsonObject.add("user", userJsonObject);
            postJsonObject.addProperty("title", post.getTitle());
            postJsonObject.addProperty("announce", announceText.length() > announceLimit ? announceText.substring(0, announceLimit) : announceText);
            postJsonObject.addProperty("likeCount", likeAndDislikeCount(postId, voteRepository.findAll())[0]);
            postJsonObject.addProperty("dislikeCount", likeAndDislikeCount(postId, voteRepository.findAll())[1]);
            postJsonObject.addProperty("commentCount", commentCount(postId, commentRepository.findAll()));
            postJsonObject.addProperty("viewCount", post.getViewCount());
            jsonArray.add(postJsonObject);
        }
        jsonObject.addProperty("count", postCount);
        jsonObject.add("posts", jsonArray);

        return jsonObject.toString();
    }
    private int commentCount(int postId, Iterable<PostComment> postCommentIterable){
        int commentCount = 0;
        for (PostComment postComment : postCommentIterable){
            if(postId == postComment.getPostId().getId()){
                commentCount++;
            }
        }
        return commentCount;
    }
    private int[] likeAndDislikeCount(int postId, Iterable<PostVote> postVoteIterable){
        int[] array = new int[] {0 ,0};
        for(PostVote postVote : postVoteIterable){
            if(postId == postVote.getPost().getId()){
                if(postVote.isValue()){
                    array[0]++;
                }else {
                    array[1]++;
                }
            }
        }
        return array;
    }
    private int likeCount(int postId, Iterable<PostVote> postVoteIterable){
        int count = 0;
        for(PostVote postVote : postVoteIterable){
            if(postId == postVote.getPost().getId()){
                if(postVote.isValue()){
                    count++;
                }
            }
        }
        return count;
    }
    private int dislikeCount(int postId, Iterable<PostVote> postVoteIterable){
        int count = 0;
        for(PostVote postVote : postVoteIterable){
            if(postId == postVote.getPost().getId()){
                if(!postVote.isValue()){
                    count++;
                }
            }
        }
        return count;
    }
    @GetMapping("/api/tag")
    public String apiTag(){
        Iterable<Tag> tagIterable = tagRepository.findAll();
        Iterable<Tag2Post> tag2PostsIterable = tag2PostRepository.findAll();
        Hashtable<String, Integer> hashTags = new Hashtable<>();
        JsonObject jsonObject = new JsonObject();
        JsonArray jsonArray = new JsonArray();
        for (Tag tag : tagIterable){
            int tagCount = 0;
            for (Tag2Post tag2Post : tag2PostsIterable){
                if(tag2Post.getTagID().getId() == tag.getId()){
                    hashTags.put(tag.getName(), ++tagCount);
                }
            }
        }
        TreeMap<String, Integer> treeMap = new TreeMap<>(hashTags);
        boolean swch = true;
        double k = 0;
        for (Map.Entry entry : treeMap.entrySet()){
            JsonObject treeJsonObject = new JsonObject();
            if(swch){
                k = 1 / ((double) ((int) entry.getValue()) / hashTags.size());
                swch = false;
                treeJsonObject.addProperty("name", entry.getKey().toString());
                treeJsonObject.addProperty("weight", 1.0);
            }else {
                treeJsonObject.addProperty("name", entry.getKey().toString());
                treeJsonObject.addProperty("weight", Math.round(k * ((double) ((int) entry.getValue()) / hashTags.size()) * 100.0) / 100.0);
            }
            jsonArray.add(treeJsonObject);
        }
        jsonObject.add("tags", jsonArray);
        return jsonObject.toString();
    }
    @GetMapping("/api/post/search")
    public String postSearch(){
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("result", false);
        return jsonObject.toString();
    }
}
