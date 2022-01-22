package main.service;

import lombok.RequiredArgsConstructor;
import main.api.response.ApiCalendarResponse;
import main.dto.PostCalendarDtoRepository;
import main.repository.PostRepository;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import java.text.SimpleDateFormat;
import java.util.*;

@RequiredArgsConstructor
@Service
public class ApiCalendarService {

    private final PostRepository postRepo;

    public ApiCalendarResponse getCalendar(String year){

        return calendar(year);
    }

    public ApiCalendarResponse getCalendar(){

        return calendar(new SimpleDateFormat("yyyy").format(System.currentTimeMillis()));
    }

    public ApiCalendarResponse calendar(String year){

        ApiCalendarResponse apiCalendarResponse = new ApiCalendarResponse();
        Hashtable<String, Integer> hashtable = new Hashtable<>();
        List<PostCalendarDtoRepository> postDtoIterable = postRepo.findDate("%" + year + "%", PageRequest.of(0, 10));
        SimpleDateFormat dt1 = new SimpleDateFormat("yyyy-MM-dd");
        SimpleDateFormat dt2 = new SimpleDateFormat("yyyy");
        HashSet<String> years = new HashSet<>();

        for (PostCalendarDtoRepository post : postDtoIterable){

            years.add(dt2.format(post.getTime()));
            hashtable.put(dt1.format(post.getTime()), (int) post.getCount());
        }

        apiCalendarResponse.setYears(years);
        apiCalendarResponse.setPosts(hashtable);

        return apiCalendarResponse;
    }

}
