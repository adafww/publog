package main;

import main.dto.TagDto;
import main.dto.TagForDtoRepository;
import main.repository.Tag2PostRepository;
import main.repository.TagRepository;
import main.service.TagService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import java.util.ArrayList;
import java.util.List;

@ExtendWith(MockitoExtension.class)
@DisplayName("Тестирование TagService")
public class TagServiceTest {

    @Mock
    private TagRepository tagRepo;

    @Mock
    private Tag2PostRepository tag2PostRepo;

    @Test
    @DisplayName("Успешный ответ метода getTagResponse")
    void getTagResponse(){

        int count = 0;
        List<TagDto> tags = new ArrayList<>();
        TagForDtoRepository tagJava = new TagForDtoRepository("Java", 18);
        TagForDtoRepository tagSpring = new TagForDtoRepository("Spring", 10);
        TagForDtoRepository tagHibernate = new TagForDtoRepository("Hibernate", 4);
        TagForDtoRepository tagHadoop = new TagForDtoRepository("Hadoop", 3);
        List<TagForDtoRepository> tagForDtoRepositoryList = new ArrayList<>();

        tags.add(new TagDto("Java", 1));
        tags.add(new TagDto("Spring", 0.56));
        tags.add(new TagDto("Hibernate", 0.22));
        tags.add(new TagDto("Hadoop", 0.17));

        tagForDtoRepositoryList.add(tagJava);
        tagForDtoRepositoryList.add(tagSpring);
        tagForDtoRepositoryList.add(tagHibernate);
        tagForDtoRepositoryList.add(tagHadoop);

        Mockito.when(tag2PostRepo.count()).thenReturn(20L);
        Mockito.when(tagRepo.findAllForDto()).thenReturn(tagForDtoRepositoryList);

        List<TagDto> tagsTest = new TagService(tagRepo, tag2PostRepo).getTagResponse().getTags();

        for (TagDto dto : tagsTest){

            for (TagDto dto1 : tags){

                if(dto.getWeight() == dto1.getWeight()){

                    count++;
                }
            }
        }

        Assertions.assertEquals(count, 4);
    }
}
