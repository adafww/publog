package main.controller;

import lombok.RequiredArgsConstructor;
import main.api.request.CommentRequest;
import main.api.request.ModerationRequest;
import main.api.response.*;
import main.repository.GlobalSettingsRepository;
import main.service.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api")
public class ApiGeneralController {

    private final SettingsService settingsService;
    private final TagService tagService;
    private final CalendarService calendarService;
    private final CommentService commentService;
    private final StatisticsService statisticsService;
    private final PostService postService;

    private final InitResponse initResponse;

    private final GlobalSettingsRepository globalSettingsRepo;

    @GetMapping("/init")
    public InitResponse init(){
        return initResponse;
    }

    @GetMapping("/settings")
    public SettingsResponse settings(){
        return settingsService.getGlobalSettings();
    }

    @GetMapping("/tag")
    public TagResponse apiTag(){
        return tagService.getTagResponse();
    }

    @GetMapping(value = "/calendar", params = {"year"})
    public ResponseEntity<CalendarResponse> postCalendar(@RequestParam String year){
        return new ResponseEntity<>(calendarService.getCalendar(year), HttpStatus.OK);
    }

    @GetMapping(value = "/calendar")
    public ResponseEntity<CalendarResponse> postCalendar(){
        return new ResponseEntity<>(calendarService.getCalendar(), HttpStatus.OK);
    }

    @PostMapping("/comment")
    public ResponseEntity<ErrorResponse> getCommentResponse(@RequestBody CommentRequest request){

        if(request.getText().length() < 5){
            return new ResponseEntity<>(commentService.getCommentFalse(), HttpStatus.BAD_REQUEST);
        }else {
            return new ResponseEntity<>(commentService.getCommentOk(request), HttpStatus.OK);
        }
    }

    @GetMapping("/statistics/all")
    public ResponseEntity<StatisticsResponse> getAllStatistics(){
        if(globalSettingsRepo.statisticsIsAccepted()
                || SecurityContextHolder.getContext().getAuthentication().getAuthorities().size() == 2){
            return new ResponseEntity<>(statisticsService.getAll(), HttpStatus.OK);
        }else {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
    }

    @GetMapping("/statistics/my")
    public ResponseEntity<StatisticsResponse> getMyStatistics(){
        if(!SecurityContextHolder.getContext().getAuthentication().getName().equals("anonymousUser")){
            return new ResponseEntity<>(statisticsService.getMy(), HttpStatus.OK);
        }else {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
    }

    @PostMapping(value = "/moderation")
    public ResponseEntity<ErrorResponse> getModerationRequest(@RequestBody ModerationRequest request){
        if(SecurityContextHolder.getContext().getAuthentication().getAuthorities().size() == 2){
            return new ResponseEntity<>(postService.moderationPosts(request), HttpStatus.OK);
        }else {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

}
