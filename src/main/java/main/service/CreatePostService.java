package main.service;

import lombok.RequiredArgsConstructor;
import main.api.request.PostRequest;
import main.api.response.CreatePostAbstractResponse;
import main.api.response.CreatePostFailResponse;
import main.api.response.CreatePostFalseResponse;
import main.api.response.CreatePostSuccessResponse;
import main.model.*;
import main.repository.PostRepository;
import main.repository.Tag2PostRepository;
import main.repository.TagRepository;
import main.repository.UserRepository;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import java.util.Date;
import java.util.Hashtable;
import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class CreatePostService {

    private final PostRepository postRepo;
    private final UserRepository userRepo;
    private final TagRepository tagRepo;
    private final Tag2PostRepository tag2PostRepo;

    public CreatePostAbstractResponse getCreatePost(PostRequest request){

        Hashtable<String, String> errors = errors(request);

        if(errors.size() != 0){
            return new CreatePostFailResponse(errors);
        }else {
            savePost(request);
            return new CreatePostSuccessResponse();
        }
    }

    public CreatePostAbstractResponse editPost(PostRequest request, int id){

        if(
                SecurityContextHolder.getContext().getAuthentication().getAuthorities().size() == 2
                || postRepo.isAuthor(id, SecurityContextHolder.getContext().getAuthentication().getName())){

            Hashtable<String, String> errors = errors(request);

            if(errors.size() != 0){
                return new CreatePostFailResponse(errors);
            }else {
                postRepo.postUpdate(
                        id,
                        new Date(Long.parseLong(String.valueOf(request.getTimestamp()) + "000")),
                        request.getActive() == 1,
                        request.getTitle(),
                        request.getText()
                );
                updateTags(request, id);
                return new CreatePostSuccessResponse();
            }
        }else {
            return new CreatePostFalseResponse();
        }
    }

    public void savePost(PostRequest request){

        User user = userRepo.findByName(SecurityContextHolder.getContext().getAuthentication().getName());
        Post post = postRepo.save(
                new Post(
                        request.getActive() == 1,
                        ModerationStatusType.NEW,
                        user,
                        new Date(Long.parseLong(request.getTimestamp() + "000")),
                        request.getTitle(),
                        request.getText()
                ));
        saveTags(request, post);
    }

    private Hashtable<String, String> errors(PostRequest request){

        Hashtable<String, String> errors = new Hashtable<>();

        if(request.getTitle().length() < 5){
            errors.put("title", "Заголовок не установлен");
        }

        if(request.getText().length() < 50){
            errors.put("text", "Текст публикации слишком короткий");
        }

        return errors;
    }

    public void saveTags(PostRequest request, Post post){

        for (String str : request.getTags()){
            if (!tagRepo.existsByName(str)) {
                int id = tagRepo.save(new Tag(str)).getId();
                tag2PostRepo.save(new Tag2Post(post, new Tag(id, str)));
            }else {
                tag2PostRepo.saveByPostAndTagName(post.getId(), str);
            }
        }
    }

    public void updateTags(PostRequest request, int id){

        List<String> oldTagList = tagRepo.tagsByPostId(id);
        List<String> newTagList = request.getTags();

        List<String> delTagList = oldTagList
                .stream()
                .filter(a -> newTagList.stream().noneMatch(b -> b.equals(a)))
                .collect(Collectors.toList());

        List<String> saveTagList = newTagList
                .stream()
                .filter(a -> oldTagList.stream().noneMatch(b -> b.equals(a)))
                .collect(Collectors.toList());

        for (String str : saveTagList){
            if(!tagRepo.existsByName(str)){
                tag2PostRepo.saveByPostAndTagName(id, tagRepo.save(new Tag(str)).getName());
            }else {
                tag2PostRepo.saveByPostAndTagName(id, str);
            }
        }

        for(String str : delTagList){
            tag2PostRepo.delete(tag2PostRepo.findByName(id, str).get(0));
            if(tagRepo.tagsByName(str) == 0){
                tagRepo.delete(tagRepo.findByName(str));
            }
        }
    }
}
