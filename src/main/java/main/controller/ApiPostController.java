package main.controller;

import lombok.RequiredArgsConstructor;
import main.api.response.ApiPostResponse;
import main.service.ApiPostService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
public class ApiPostController {

    private final ApiPostService apiPostService;

    @GetMapping(value = "/api/post", params = {"offset", "limit", "mode"})
    public ResponseEntity<ApiPostResponse> postInfo(@RequestParam int offset, @RequestParam int limit, @RequestParam String mode){
        return new ResponseEntity<>(apiPostService.getApiPostResponse(offset, limit, mode), HttpStatus.OK);
    }
}
