package main.controller;

import lombok.RequiredArgsConstructor;
import main.api.request.PostRequest;
import main.api.request.PostVotesRequest;
import main.api.response.*;
import main.service.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/post")
public class ApiPostController {

    private final ApiPostService apiPostService;
    private final PostVotesService postVotesService;

    @PostMapping()
    public ResponseEntity<CreatePostAbstractResponse> postCreate(@RequestBody PostRequest request){
        return new ResponseEntity<>(apiPostService.getCreatePost(request), HttpStatus.OK);
    }

    @GetMapping(params = {"offset", "limit", "mode"})
    public ResponseEntity<ApiPostResponse> postInfo(@RequestParam(defaultValue = "0", required = false) int offset,
                                                    @RequestParam(defaultValue = "10", required = false) int limit,
                                                    @RequestParam(defaultValue = "recent", required = false) String mode){
        return new ResponseEntity<>(apiPostService.getApiPostResponse(offset, limit, mode), HttpStatus.OK);
    }

    @PutMapping("/{id}")
    public ResponseEntity<CreatePostAbstractResponse> postEdit(@PathVariable int id, @RequestBody PostRequest request){
        return new ResponseEntity<>(apiPostService.editPost(request, id), HttpStatus.OK);
    }

    @GetMapping(value = "/{id}")
    public ResponseEntity<ApiPostIdResponse> postById(@PathVariable int id){
        return new ResponseEntity<>(apiPostService.getPostById(id), HttpStatus.OK);
    }

    @GetMapping(value = "/search", params = {"offset", "limit", "query"})
    public ResponseEntity<ApiPostResponse> postSearch(@RequestParam int offset, @RequestParam int limit, @RequestParam String query){
        return new ResponseEntity<>(apiPostService.getApiPostSearch(offset, limit, query), HttpStatus.OK);
    }

    @GetMapping(value = "/byDate", params = {"offset", "limit", "date"})
    public ResponseEntity<ApiPostResponse> postByDate(@RequestParam int offset, @RequestParam int limit, @RequestParam String date){
        return new ResponseEntity<>(apiPostService.getPostByDate(offset, limit, date), HttpStatus.OK);
    }

    @GetMapping(value = "/byTag", params = {"offset", "limit", "tag"})
    public ResponseEntity<ApiPostResponse> postByTag(@RequestParam int offset, @RequestParam int limit, @RequestParam String tag){
        return new ResponseEntity<>(apiPostService.getPostByTag(offset, limit, tag), HttpStatus.OK);
    }

    @GetMapping(value = "/my", params = {"offset", "limit", "status"})
    public ResponseEntity<ApiPostResponse> postMy(@RequestParam(defaultValue = "0", required = false) int offset,
                                                    @RequestParam(defaultValue = "10", required = false) int limit,
                                                    @RequestParam String status) {
        return new ResponseEntity<>(apiPostService.getApiPostMyResponse(
                offset, limit,
                SecurityContextHolder
                        .getContext()
                        .getAuthentication()
                        .getName(), status),
                HttpStatus.OK);
    }

    @PostMapping("/like")
    public ResponseEntity<PostVotesResponse> like(@RequestBody PostVotesRequest request){
        if(!SecurityContextHolder.getContext().getAuthentication().getName().equals("anonymousUser")){
            return new ResponseEntity<>(postVotesService.getLike(request.getPostId()), HttpStatus.OK);
        }else {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
    }

    @PostMapping("/dislike")
    public ResponseEntity<PostVotesResponse> dislike(@RequestBody PostVotesRequest request){
        if(!SecurityContextHolder.getContext().getAuthentication().getName().equals("anonymousUser")){
            return new ResponseEntity<>(postVotesService.getDislike(request.getPostId()), HttpStatus.OK);
        }else {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
    }
}
