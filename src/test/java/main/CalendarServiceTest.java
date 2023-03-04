package main;

import main.api.response.CalendarResponse;
import main.dto.PostCalendarDtoRepository;
import main.repository.PostRepository;
import main.service.CalendarService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageRequest;
import java.text.SimpleDateFormat;
import java.util.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("Тестирование CalendarService")
public class CalendarServiceTest {

    @Mock
    private PostRepository postRepo;

    @Test
    @DisplayName("Успешный ответ без параметра")
    void responseTest(){

        Date date = new Date();
        SimpleDateFormat dt2 = new SimpleDateFormat("yyyy-MM-dd");
        SimpleDateFormat dt1 = new SimpleDateFormat("yyyy");
        String year = dt1.format(date);
        HashSet<String> hashSet = new HashSet<>();
        PostCalendarDtoRepository postCalendarDtoRepository = new PostCalendarDtoRepository(date, 1);
        List<PostCalendarDtoRepository> dtoList = new ArrayList<>();
        Hashtable<String, Integer> postsResponse = new Hashtable<>();
        CalendarResponse response = new CalendarResponse();

        hashSet.add(year);
        dtoList.add(postCalendarDtoRepository);
        postsResponse.put(dt2.format(date), 1);
        response.setYears(hashSet);
        response.setPosts(postsResponse);

        Mockito
                .when(postRepo.findDate("%" + year + "%", PageRequest.of(0, 10)))
                .thenReturn(dtoList);


        Assertions.assertEquals(response.getYears(), new CalendarService(postRepo).getCalendar().getYears());
        Assertions.assertEquals(response.getPosts(), new CalendarService(postRepo).getCalendar().getPosts());
    }

    @Test
    @DisplayName("Успешный ответ c параметром")
    void responseWithYearTest(){

        Date date = new Date();
        SimpleDateFormat dt2 = new SimpleDateFormat("yyyy-MM-dd");
        SimpleDateFormat dt1 = new SimpleDateFormat("yyyy");
        String year = dt1.format(date);
        HashSet<String> hashSet = new HashSet<>();
        PostCalendarDtoRepository postCalendarDtoRepository = new PostCalendarDtoRepository(date, 1);
        List<PostCalendarDtoRepository> dtoList = new ArrayList<>();
        Hashtable<String, Integer> postsResponse = new Hashtable<>();
        CalendarResponse response = new CalendarResponse();

        hashSet.add(year);
        dtoList.add(postCalendarDtoRepository);
        postsResponse.put(dt2.format(date), 1);
        response.setYears(hashSet);
        response.setPosts(postsResponse);


        Mockito
                .when(postRepo.findDate("%" + year + "%", PageRequest.of(0, 10)))
                .thenReturn(dtoList);


        Assertions.assertEquals(response.getYears(), new CalendarService(postRepo).getCalendar(year).getYears());
        Assertions.assertEquals(response.getPosts(), new CalendarService(postRepo).getCalendar(year).getPosts());
    }
}
//CaptchaCode captchaCodeToReturn = new CaptchaCode(new Date(), "test", "test");
//Mockito.when(codeRepository.save(any(CaptchaCode.class))).thenReturn(captchaCodeToReturn);


