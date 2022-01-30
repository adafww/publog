package main.service;

import lombok.RequiredArgsConstructor;
import main.api.request.ModerationRequest;
import main.api.request.PostRequest;
import main.api.response.*;
import main.dto.*;
import main.model.*;
import main.model.enums.ModerationStatusType;
import main.repository.*;
import org.jsoup.Jsoup;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class PostService {

    private final PostRepository postRepo;
    private final UserRepository userRepo;
    private final PostCommentRepository postCommentRepo;
    private final Tag2PostRepository tag2PostRepo;
    private final TagRepository tagRepo;
    private final GlobalSettingsRepository globalSettingsRepo;

    public ErrorResponse moderationPosts(ModerationRequest request, String modEmail){

        if(request.getDecision().equals("accept")){

            postRepo.moderationStatus(modEmail, request.getPostId(), ModerationStatusType.ACCEPTED);
        }else if(request.getDecision().equals("decline")){

            postRepo.moderationStatus(modEmail, request.getPostId(), ModerationStatusType.DECLINED);
        }

        return new ErrorResponse(true);
    }
    public PostResponse getModerationPosts(int offset, int limit, String status, String email){

        List<PostForDtoRepository> postForDtoRepositoryList = null;

        if(status.equals("new")){

            postForDtoRepositoryList = postRepo.findNewActivePosts(PageRequest.of(offset, limit));
        }else if(status.equals("declined")){

            postForDtoRepositoryList = postRepo.findModerationPosts(ModerationStatusType.DECLINED, email, PageRequest.of(offset, limit));
        }else if(status.equals("accepted")){

            postForDtoRepositoryList = postRepo.findModerationPosts(ModerationStatusType.ACCEPTED, email, PageRequest.of(offset, limit));
        }

        PostResponse postResponse = new PostResponse();
        assert postForDtoRepositoryList != null;
        postResponse.setCount(postForDtoRepositoryList.size());
        postResponse.setPosts(postDtoList(postForDtoRepositoryList));

        return postResponse;
    }

    public PostResponse getApiPostMyResponse(int offset, int limit, String email, String status){

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

        PostResponse postResponse = new PostResponse();
        assert postForDtoRepositoryList != null;
        postResponse.setCount(postForDtoRepositoryList.size());
        postResponse.setPosts(postDtoList(postForDtoRepositoryList));

        return postResponse;
    }

    public PostResponse getApiPostResponse(int offset, int limit, String mode) {

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

        PostResponse postResponse = new PostResponse();
        postResponse.setCount(postDtoIterable.size());
        postResponse.setPosts(postDtoList(postDtoIterable));

        return postResponse;
    }

    public PostResponse getApiPostSearch(int offset, int limit, String query){

        List<PostForDtoRepository> postDtoIterable = postRepo.findPostSearch("%" + query + "%", PageRequest.of(offset, limit));
        PostResponse postResponse = new PostResponse();
        postResponse.setCount(postDtoIterable.size());
        postResponse.setPosts(postDtoList(postDtoIterable));

        return postResponse;
    }

    public PostIdResponse getPostById(int id, String userEmail, int isMod){

        PostIdResponse postIdResponse = new PostIdResponse();
        PostForDtoRepository postDtoIterable = postRepo.findPostId(id).stream().findFirst().get();
        List<CommentForPostForDto> comPostForDto = postCommentRepo.findPostCommentById(id);
        List<CommentPostDto> commentPostDtoList = new ArrayList<>();
        CommentPostDto commentPostDto;

        //view count
        if(isMod == 1 && !postRepo.isAuthor(id, userEmail)){

            postRepo.incrementViewById(id);
        }

        for(CommentForPostForDto postComment : comPostForDto){

            commentPostDto = new CommentPostDto();
            commentPostDto.setId(postComment.getId());
            commentPostDto.setTimestamp(Long.parseLong(Long.toString(postComment.getTimestamp().getTime()).substring(0, Long.toString(postComment.getTimestamp().getTime()).length() - 3)));
            commentPostDto.setText(postComment.getText());
            commentPostDto.setUser(userRepo.findByIdWithPhoto(postComment.getUserId()).stream().findFirst().get());
            commentPostDtoList.add(commentPostDto);
        }

        postIdResponse.setId(postDtoIterable.getId());
        postIdResponse.setTimestamp(Long.parseLong(Long.toString(postDtoIterable.getTimestamp().getTime()).substring(0, Long.toString(postDtoIterable.getTimestamp().getTime()).length() - 3)));
        postIdResponse.setUser(userRepo.findById(postDtoIterable.getUserId()).get(0));
        postIdResponse.setTitle(postDtoIterable.getTitle());
        postIdResponse.setText(Jsoup.parse(postDtoIterable.getAnnounce()).text());
        postIdResponse.setLikeCount(postDtoIterable.getLikeCount());
        postIdResponse.setDislikeCount(postDtoIterable.getDislikeCount());
        postIdResponse.setCommentCount(postDtoIterable.getCommentCount());
        postIdResponse.setViewCount(postDtoIterable.getViewCount());
        postIdResponse.setComments(commentPostDtoList);
        postIdResponse.setTags(tag2PostRepo.getTags(id));

        return postIdResponse;
    }

    public PostResponse getPostByTag(int offset, int limit, String tag){

        List<PostForDtoRepository> postDtoIterable = postRepo.findByTag(tag, PageRequest.of(offset, limit));

        PostResponse postResponse = new PostResponse();
        postResponse.setCount(postDtoIterable.size());
        postResponse.setPosts(postDtoList(postDtoIterable));

        return postResponse;
    }

    public PostResponse getPostByDate(int offset, int limit, String date){

        List<PostForDtoRepository> postDtoIterable = postRepo.findByDate("%" + date + "%", PageRequest.of(offset, limit));

        PostResponse postResponse = new PostResponse();
        postResponse.setCount(postDtoIterable.size());
        postResponse.setPosts(postDtoList(postDtoIterable));

        return postResponse;
    }

    private List<PostDto> postDtoList(List<PostForDtoRepository> listDto){

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

    public ErrorResponse getCreatePost(PostRequest request){

        Hashtable<String, String> errors = errors(request);

        if(errors.size() != 0){

            return new ErrorResponse(false, errors);
        }else {

            savePost(request);
            return new ErrorResponse(true);
        }
    }

    public ErrorResponse editPost(PostRequest request, int postId){

        int userStatus = SecurityContextHolder.getContext().getAuthentication().getAuthorities().size();
        String userName = SecurityContextHolder.getContext().getAuthentication().getName();

        if(userStatus == 2 || postRepo.isAuthor(postId, userName)){

            Hashtable<String, String> errors = errors(request);

            if(errors.size() != 0){

                return new ErrorResponse(false, errors);
            }else {

                if(userStatus == 2){

                    postRepo.postModUpdate(
                            moderationStatusType(),
                            userName,
                            postId,
                            new Date(Long.parseLong(request.getTimestamp() + "000")),
                            request.getActive() == 1,
                            request.getTitle(),
                            request.getText()
                    );
                }else {

                    postRepo.postUpdate(
                            moderationStatusType(),
                            postId,
                            new Date(Long.parseLong(request.getTimestamp() + "000")),
                            request.getActive() == 1,
                            request.getTitle(),
                            request.getText()
                    );
                }
                updateTags(request, postId);

                return new ErrorResponse(true);
            }
        }else {

            return new ErrorResponse(false);
        }
    }

    private void savePost(PostRequest request){

        User user = userRepo.findByName(SecurityContextHolder.getContext().getAuthentication().getName());
        Post post = postRepo.save(
                new Post(
                        request.getActive() == 1,
                        moderationStatusType(),
                        user,
                        new Date(Long.parseLong(request.getTimestamp() + "000")),
                        request.getTitle(),
                        request.getText()
                ));
        saveTags(request, post);
    }

    private ModerationStatusType moderationStatusType(){

        if(globalSettingsRepo.isPostPremoderation()){

            return ModerationStatusType.NEW;
        }else {

            return ModerationStatusType.ACCEPTED;
        }
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

    private void saveTags(PostRequest request, Post post){

        for (String str : request.getTags()){

            if (!tagRepo.existsByName(str)) {

                int id = tagRepo.save(new Tag(str)).getId();
                tag2PostRepo.save(new Tag2Post(post, new Tag(id, str)));
            }else {

                tag2PostRepo.saveByPostAndTagName(post.getId(), str);
            }
        }
    }

    private void updateTags(PostRequest request, int id){

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
