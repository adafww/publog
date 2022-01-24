package main.controller;

import lombok.RequiredArgsConstructor;
import main.api.request.CommentRequest;
import main.api.request.ModerationRequest;
import main.api.request.ProfileRequest;
import main.api.response.*;
import main.repository.GlobalSettingsRepository;
import main.service.*;
import org.hibernate.annotations.Parameter;
import org.imgscalr.Scalr;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.Nullable;
import org.springframework.mail.MailException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import javax.imageio.stream.FileImageInputStream;
import javax.servlet.annotation.MultipartConfig;
import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

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
    private final ProfileService profileService;

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

    @PostMapping(value = "/profile/my", consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
    public ResponseEntity<ErrorResponse> getDataProfile(@RequestParam(value = "photo") MultipartFile photo,
                                                        @RequestParam(value = "name") String name,
                                                        @RequestParam(value = "email") String email,
                                                        @RequestParam(value = "password", required = false) String password,
                                                        @RequestParam(value = "removePhoto") int removePhoto) throws IOException {

        System.out.println(name);
        System.out.println(photo.getContentType());
        System.out.println(Scalr.resize(ImageIO.read(new ByteArrayInputStream(photo.getBytes())), Scalr.Method.ULTRA_QUALITY,
        Scalr.Mode.FIT_EXACT, 36, 36).getWidth());

        if(!SecurityContextHolder.getContext().getAuthentication().getName().equals("anonymousUser")){

            return new ResponseEntity<>(HttpStatus.OK);
        }else {

            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
    }

    @PostMapping(value = "/image")
    public ResponseEntity<String> uploadImage(){

        System.out.println("!!!");
        return null;
    }
}
