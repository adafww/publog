package main.service;

import lombok.RequiredArgsConstructor;
import main.api.response.TagResponse;
import main.dto.TagDto;
import main.model.Tag;
import main.model.Tag2Post;
import main.repository.Tag2PostRepository;
import main.repository.TagRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@RequiredArgsConstructor
@Service
public class TagService {

    private final TagRepository tagRepo;
    private final Tag2PostRepository tag2PostRepo;

    public TagResponse getTagResponse(){
        boolean swch = true;
        double k = 0;
        ArrayList<TagDto> dtoList = new ArrayList<>();
        TagResponse tagResponse = new TagResponse();
        TagDto dto;
        int length = ((ArrayList<Tag2Post>) tag2PostRepo.findAll()).size();
        for(Tag tag : tagRepo.findSortedObject()){
            dto = new TagDto();
            if(swch){
                k = 1 / ((double) (tagCount(tag2PostRepo.findAll(), tag.getId())) / length);
                swch = false;
                dto.setName(tag.getName());
                dto.setWeight(1.0);
            }else {
                dto.setName(tag.getName());
                dto.setWeight(Math.round(k * ((double) (tagCount(tag2PostRepo.findAll(), tag.getId())) / length) * 100.0) / 100.0);
            }
            dtoList.add(dto);
        }
        tagResponse.setTags(dtoList);
        return tagResponse;
    }
    private int tagCount(Iterable<Tag2Post> tag2PostRepo, int id){
        int count = 0;
        for (Tag2Post tag2Post : tag2PostRepo){
            if(tag2Post.getTagID().getId() == id){
                count++;
            }
        }
        return count;
    }
}
