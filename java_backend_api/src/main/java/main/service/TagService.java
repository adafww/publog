package main.service;

import lombok.RequiredArgsConstructor;
import main.api.response.TagResponse;
import main.dto.TagDto;
import main.repository.Tag2PostRepository;
import main.dto.TagForDtoRepository;
import main.repository.TagRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
@Service
public class TagService {

    private final TagRepository tagRepo;
    private final Tag2PostRepository tag2PostRepo;

    public TagResponse getTagResponse(){

        long length = tag2PostRepo.count();
        List<TagForDtoRepository> tagForDtoRepositoryList = tagRepo.findAllForDto();
        boolean swch = true;
        double k = 0;
        ArrayList<TagDto> dtoList = new ArrayList<>();
        TagResponse tagResponse = new TagResponse();
        TagDto dto;

        for(TagForDtoRepository tag : tagForDtoRepositoryList){

            dto = new TagDto();
            if(swch){

                k = 1 / ((double) (tag.getCount()) / length);
                swch = false;
                dto.setName(tag.getName());
                dto.setWeight(1.0);
            }else {

                dto.setName(tag.getName());
                dto.setWeight(Math.round(k * ((double) (tag.getCount()) / length) * 100.0) / 100.0);
            }
            dtoList.add(dto);
        }
        tagResponse.setTags(dtoList);

        return tagResponse;
    }
}
