package main.controller;

import lombok.RequiredArgsConstructor;
import main.api.request.CommentRequest;
import main.api.request.PostRequest;
import main.api.request.PostVotesRequest;
import main.api.response.*;
import main.model.Role;
import main.service.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
public class ApiPostController {

    private final ApiPostService apiPostService;
    private final ApiPostMyService apiPostMyService;
    private final ApiCalendarService apiCalendarService;
    private final CreatePostService createPostService;
    private final CommentService commentService;
    private final PostVotesService postVotesService;

    @GetMapping(value = "/api/post", params = {"offset", "limit", "mode"})
    public ResponseEntity<ApiPostResponse> postInfo(@RequestParam(defaultValue = "0", required = false) int offset,
                                                    @RequestParam(defaultValue = "10", required = false) int limit,
                                                    @RequestParam(defaultValue = "recent", required = false) String mode){
        return new ResponseEntity<>(apiPostService.getApiPostResponse(offset, limit, mode), HttpStatus.OK);
    }

    @PostMapping("/api/post")
    public ResponseEntity<CreatePostAbstractResponse> postCreate(@RequestBody PostRequest request){
        return new ResponseEntity<>(createPostService.getCreatePost(request), HttpStatus.OK);
    }

    @PutMapping("/api/post/{ID}")
    public ResponseEntity<CreatePostAbstractResponse> postEdit(@PathVariable int ID, @RequestBody PostRequest request){
        return new ResponseEntity<>(createPostService.editPost(request, ID), HttpStatus.OK);
    }

    @GetMapping(value = "/api/post/{ID}")
    public ResponseEntity<ApiPostIdResponse> postById(@PathVariable int ID){
        return new ResponseEntity<>(apiPostService.getPostById(ID), HttpStatus.OK);
    }

    @PostMapping("/api/comment")
    public ResponseEntity<CommentAbstractResponse> getCommentResponse(@RequestBody CommentRequest request){

        if(request.getText().length() < 5){
            return new ResponseEntity<>(commentService.getCommentFalse(), HttpStatus.BAD_REQUEST);
        }else {
            return new ResponseEntity<>(commentService.getCommentOk(request), HttpStatus.OK);
        }
    }

    @GetMapping(value = "/api/post/search", params = {"offset", "limit", "query"})
    public ResponseEntity<ApiPostResponse> postSearch(@RequestParam int offset, @RequestParam int limit, @RequestParam String query){
        return new ResponseEntity<>(apiPostService.getApiPostSearch(offset, limit, query), HttpStatus.OK);
    }

    @GetMapping(value = "/api/calendar", params = {"year"})
    public ResponseEntity<ApiCalendarResponse> postCalendar(@RequestParam String year){
        return new ResponseEntity<>(apiCalendarService.getCalendar(year), HttpStatus.OK);
    }

    @GetMapping(value = "/api/calendar")
    public ResponseEntity<ApiCalendarResponse> postCalendar(){
        return new ResponseEntity<>(apiCalendarService.getCalendar(), HttpStatus.OK);
    }

    @GetMapping(value = "/api/post/byDate", params = {"offset", "limit", "date"})
    public ResponseEntity<ApiPostResponse> postByDate(@RequestParam int offset, @RequestParam int limit, @RequestParam String date){
        return new ResponseEntity<>(apiPostService.getPostByDate(offset, limit, date), HttpStatus.OK);
    }

    @GetMapping(value = "/api/post/byTag", params = {"offset", "limit", "tag"})
    public ResponseEntity<ApiPostResponse> postByTag(@RequestParam int offset, @RequestParam int limit, @RequestParam String tag){
        return new ResponseEntity<>(apiPostService.getPostByTag(offset, limit, tag), HttpStatus.OK);
    }

    @GetMapping(value = "/api/post/my", params = {"offset", "limit", "status"})
    public ResponseEntity<ApiPostMyResponse> postMy(@RequestParam(defaultValue = "0", required = false) int offset,
                                                    @RequestParam(defaultValue = "10", required = false) int limit,
                                                    @RequestParam String status) {
        return new ResponseEntity<>(apiPostMyService.getApiPostMyResponse(
                offset, limit,
                SecurityContextHolder
                        .getContext()
                        .getAuthentication()
                        .getName(), status),
                HttpStatus.OK);
    }
    @PostMapping("/api/post/like")
    public ResponseEntity<PostVotesResponse> like(@RequestBody PostVotesRequest request){
        if(!SecurityContextHolder.getContext().getAuthentication().getName().equals("anonymousUser")){
            return new ResponseEntity<>(postVotesService.getLike(request.getPostId()), HttpStatus.OK);
        }else {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
    }

    @PostMapping("/api/post/dislike")
    public ResponseEntity<PostVotesResponse> dislike(@RequestBody PostVotesRequest request){
        if(!SecurityContextHolder.getContext().getAuthentication().getName().equals("anonymousUser")){
            return new ResponseEntity<>(postVotesService.getDislike(request.getPostId()), HttpStatus.OK);
        }else {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
    }
}
