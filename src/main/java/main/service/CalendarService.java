package main.service;

import lombok.RequiredArgsConstructor;
import main.api.response.CalendarResponse;
import main.dto.PostCalendarDtoRepository;
import main.repository.PostRepository;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import java.text.SimpleDateFormat;
import java.util.*;

@RequiredArgsConstructor
@Service
public class CalendarService {

    private final PostRepository postRepo;

    public CalendarResponse getCalendar(String year){

        return calendar(year);
    }

    public CalendarResponse getCalendar(){

        return calendar(new SimpleDateFormat("yyyy").format(System.currentTimeMillis()));
    }

    private CalendarResponse calendar(String year){

        CalendarResponse calendarResponse = new CalendarResponse();
        Hashtable<String, Integer> hashtable = new Hashtable<>();
        List<PostCalendarDtoRepository> postDtoIterable = postRepo.findDate("%" + year + "%", PageRequest.of(0, 10));
        SimpleDateFormat dt1 = new SimpleDateFormat("yyyy-MM-dd");
        SimpleDateFormat dt2 = new SimpleDateFormat("yyyy");
        HashSet<String> years = new HashSet<>();

        for (PostCalendarDtoRepository post : postDtoIterable){

            years.add(dt2.format(post.getTime()));
            hashtable.put(dt1.format(post.getTime()), (int) post.getCount());
        }

        calendarResponse.setYears(years);
        calendarResponse.setPosts(hashtable);

        return calendarResponse;
    }

}
