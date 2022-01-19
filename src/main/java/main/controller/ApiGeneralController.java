package main.controller;

import lombok.RequiredArgsConstructor;
import main.api.request.CommentRequest;
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
    private final ApiCalendarService apiCalendarService;
    private final CommentService commentService;
    private final ApiStatisticsService apiStatisticsService;

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
    public ResponseEntity<ApiCalendarResponse> postCalendar(@RequestParam String year){
        return new ResponseEntity<>(apiCalendarService.getCalendar(year), HttpStatus.OK);
    }

    @GetMapping(value = "/calendar")
    public ResponseEntity<ApiCalendarResponse> postCalendar(){
        return new ResponseEntity<>(apiCalendarService.getCalendar(), HttpStatus.OK);
    }

    @PostMapping("/comment")
    public ResponseEntity<CommentAbstractResponse> getCommentResponse(@RequestBody CommentRequest request){

        if(request.getText().length() < 5){
            return new ResponseEntity<>(commentService.getCommentFalse(), HttpStatus.BAD_REQUEST);
        }else {
            return new ResponseEntity<>(commentService.getCommentOk(request), HttpStatus.OK);
        }
    }

    @GetMapping("/statistics/all")
    public ResponseEntity<ApiStatisticsResponse> getAllStatistics(){
        if(globalSettingsRepo.statisticsIsAccepted()
                || SecurityContextHolder.getContext().getAuthentication().getAuthorities().size() == 2){
            return new ResponseEntity<>(apiStatisticsService.getAll(), HttpStatus.OK);
        }else {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
    }

    @GetMapping("/statistics/my")
    public ResponseEntity<ApiStatisticsResponse> getMyStatistics(){
        if(!SecurityContextHolder.getContext().getAuthentication().getName().equals("anonymousUser")){
            return new ResponseEntity<>(apiStatisticsService.getMy(), HttpStatus.OK);
        }else {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
    }

}
