package main.controller;

import lombok.RequiredArgsConstructor;
import main.api.request.*;
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
    private final ImageService imageService;

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

    @PutMapping("/settings")
    public ResponseEntity<Object>editSettings(@RequestBody SettingsRequest request){

        if(SecurityContextHolder.getContext().getAuthentication().getAuthorities().size() == 2){

            settingsService.editSettings(request);

            return new ResponseEntity<>(HttpStatus.OK);
        }else{
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }

    }

    @GetMapping("/tag")
    public TagResponse apiTag(){
        return tagService.getTagResponse();
    }

    @GetMapping(value = "/calendar", params = {"year"})
    public ResponseEntity<CalendarResponse> postCalendar(@RequestParam String year){

        return ResponseEntity.ok(calendarService.getCalendar(year));
    }

    @GetMapping(value = "/calendar")
    public ResponseEntity<CalendarResponse> postCalendar(){

        return ResponseEntity.ok(calendarService.getCalendar());
    }

    @PostMapping("/comment")
    public ResponseEntity<Object> getCommentResponse(@RequestBody CommentRequest request){

        if(request.getText().length() < 5){

            return new ResponseEntity<>(commentService.getCommentFalse(), HttpStatus.BAD_REQUEST);
        }else {

            return new ResponseEntity<>(commentService.getCommentOk(request,
                    SecurityContextHolder.getContext().getAuthentication().getName()), HttpStatus.OK);
        }
    }

    @GetMapping("/statistics/all")
    public ResponseEntity<StatisticsResponse> getAllStatistics(){

        if(globalSettingsRepo.statisticsIsAccepted()
                || SecurityContextHolder.getContext().getAuthentication().getAuthorities().size() == 2){

            return ResponseEntity.ok(statisticsService.getAll());
        }else {

            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
    }

    @GetMapping("/statistics/my")
    public ResponseEntity<StatisticsResponse> getMyStatistics(){

        String email = SecurityContextHolder.getContext().getAuthentication().getName();


        if(!email.equals("anonymousUser")){

            return ResponseEntity.ok(statisticsService.getMy(email));
        }else {

            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
    }

    @PostMapping(value = "/moderation")
    public ResponseEntity<ErrorResponse> getModerationRequest(@RequestBody ModerationRequest request){

        if(SecurityContextHolder.getContext().getAuthentication().getAuthorities().size() == 2){

            return ResponseEntity.ok(postService.moderationPosts(
                    request,
                    SecurityContextHolder.getContext().getAuthentication().getName()));
        }else {

            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping(value = "/profile/my", consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
    public ResponseEntity<ErrorResponse> editProfile(@ModelAttribute ProfileWithPhotoRequest request) throws IOException {

        if(!SecurityContextHolder.getContext().getAuthentication().getName().equals("anonymousUser")){

            return ResponseEntity.ok(profileService.editProfile(request,
                    SecurityContextHolder.getContext().getAuthentication().getName()));
        }else {

            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
    }

    @PostMapping(value = "/profile/my", consumes = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<ErrorResponse> editProfile(@RequestBody ProfileRequest request) throws IOException {

        String userEmail = SecurityContextHolder.getContext().getAuthentication().getName();

        if(!userEmail.equals("anonymousUser")){

            return ResponseEntity.ok(profileService.editProfile(
                    new ProfileWithPhotoRequest(
                    request.getName(),
                    request.getEmail(),
                    request.getPassword(),
                    request.getRemovePhoto()), userEmail));
        }else {

            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
    }

    @PostMapping(value = "/image", consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
    public ResponseEntity<Object> uploadImage(@RequestParam(value = "image") MultipartFile image) throws IOException {

        if(!SecurityContextHolder.getContext().getAuthentication().getName().equals("anonymousUser")){

            return ResponseEntity.ok(imageService.upload(image));
        }else {

            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
    }
}
