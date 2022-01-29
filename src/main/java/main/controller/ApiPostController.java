package main.controller;

import lombok.RequiredArgsConstructor;
import main.api.request.PostRequest;
import main.api.request.PostVotesRequest;
import main.api.response.*;
import main.repository.PostRepository;
import main.service.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/post")
public class ApiPostController {

    private final PostService postService;
    private final PostVotesService postVotesService;

    private final PostRepository postRepo;

    @PostMapping()
    public ResponseEntity<ErrorResponse> postCreate(@RequestBody PostRequest request){

        if(!SecurityContextHolder.getContext().getAuthentication().getName().equals("anonymousUser")){

            return new ResponseEntity<>(postService.getCreatePost(request), HttpStatus.OK);
        }else {

            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<ErrorResponse> postEdit(@PathVariable int id, @RequestBody PostRequest request){

        if(postRepo.isAuthor(id, SecurityContextHolder.getContext().getAuthentication().getName())){

            return new ResponseEntity<>(postService.editPost(request, id), HttpStatus.OK);
        }else {

            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping(params = {"offset", "limit", "mode"})
    public ResponseEntity<PostResponse> postInfo(@RequestParam(defaultValue = "0", required = false) int offset,
                                                 @RequestParam(defaultValue = "10", required = false) int limit,
                                                 @RequestParam(defaultValue = "recent", required = false) String mode){

        return new ResponseEntity<>(postService.getApiPostResponse(offset, limit, mode), HttpStatus.OK);
    }

    @GetMapping(value = "/{id}")
    public ResponseEntity<PostIdResponse> postById(@PathVariable int id){

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        return new ResponseEntity<>(postService.getPostById(id, authentication.getName(), authentication.getAuthorities().size()), HttpStatus.OK);
    }

    @GetMapping(value = "/search", params = {"offset", "limit", "query"})
    public ResponseEntity<PostResponse> postSearch(@RequestParam int offset,
                                                   @RequestParam int limit,
                                                   @RequestParam String query){

        return new ResponseEntity<>(postService.getApiPostSearch(offset, limit, query), HttpStatus.OK);
    }

    @GetMapping(value = "/byDate", params = {"offset", "limit", "date"})
    public ResponseEntity<PostResponse> postByDate(@RequestParam int offset, @RequestParam int limit, @RequestParam String date){

        return new ResponseEntity<>(postService.getPostByDate(offset, limit, date), HttpStatus.OK);
    }

    @GetMapping(value = "/byTag", params = {"offset", "limit", "tag"})
    public ResponseEntity<PostResponse> postByTag(@RequestParam int offset, @RequestParam int limit, @RequestParam String tag){

        return new ResponseEntity<>(postService.getPostByTag(offset, limit, tag), HttpStatus.OK);
    }

    @GetMapping(value = "/my", params = {"offset", "limit", "status"})
    public ResponseEntity<PostResponse> postMy(@RequestParam(defaultValue = "0", required = false) int offset,
                                               @RequestParam(defaultValue = "10", required = false) int limit,
                                               @RequestParam String status) {

        return new ResponseEntity<>(postService.getApiPostMyResponse(
                offset, limit,
                SecurityContextHolder
                        .getContext()
                        .getAuthentication()
                        .getName(), status),
                HttpStatus.OK);
    }

    @PostMapping("/like")
    public ResponseEntity<ErrorResponse> like(@RequestBody PostVotesRequest request){

        if(!SecurityContextHolder.getContext().getAuthentication().getName().equals("anonymousUser")){

            return new ResponseEntity<>(postVotesService.getLike(request.getPostId()), HttpStatus.OK);
        }else {

            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
    }

    @PostMapping("/dislike")
    public ResponseEntity<ErrorResponse> dislike(@RequestBody PostVotesRequest request){

        if(!SecurityContextHolder.getContext().getAuthentication().getName().equals("anonymousUser")){

            return new ResponseEntity<>(postVotesService.getDislike(request.getPostId()), HttpStatus.OK);
        }else {

            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
    }

    @GetMapping(value = "/moderation", params = {"offset", "limit", "status"})
    public ResponseEntity<PostResponse> getModerationPosts(@RequestParam(defaultValue = "0", required = false) int offset,
                                                           @RequestParam(defaultValue = "10", required = false) int limit,
                                                           @RequestParam String status){

        if(SecurityContextHolder.getContext().getAuthentication().getAuthorities().size() == 2){

            return new ResponseEntity<>(postService.getModerationPosts(offset, limit, status,
                    SecurityContextHolder.getContext().getAuthentication().getName()), HttpStatus.OK);
        }else {

            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }
}
