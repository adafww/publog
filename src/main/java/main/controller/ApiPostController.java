package main.controller;

import lombok.RequiredArgsConstructor;
import main.api.response.ApiCalendarResponse;
import main.api.response.ApiPostIdResponse;
import main.api.response.ApiPostResponse;
import main.service.ApiPostService;
import main.service.ApiCalendarService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
public class ApiPostController {

    private final ApiPostService apiPostService;
    private final ApiCalendarService apiCalendarService;

    @GetMapping(value = "/api/post", params = {"offset", "limit", "mode"})
    @PreAuthorize("hasAuthority('user:write')")
    public ResponseEntity<ApiPostResponse> postInfo(@RequestParam int offset, @RequestParam int limit, @RequestParam String mode){
        return new ResponseEntity<>(apiPostService.getApiPostResponse(offset, limit, mode), HttpStatus.OK);
    }

    @GetMapping(value = "/api/post/search", params = {"offset", "limit", "query"})
    @PreAuthorize("hasAuthority('user:moderate')")
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

    @GetMapping(value = "/api/post/{ID}")
    public ResponseEntity<ApiPostIdResponse> postById(@PathVariable int ID){
        return new ResponseEntity<>(apiPostService.getPostById(ID), HttpStatus.NOT_FOUND);
    }
}
