package com.cezaram28.Assignment1.service;

import com.cezaram28.Assignment1.entity.Tag;
import com.cezaram28.Assignment1.exception.TagNotFoundException;
import com.cezaram28.Assignment1.repository.RepositoryFactory;
import com.cezaram28.Assignment1.repository.TagRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class TagManagementService {

    private final RepositoryFactory repositoryFactory;

    @Transactional
    public Tag addTag(Tag tag){ return repositoryFactory.createTagRepository().save(tag);}

    @Transactional
    public void remove(int id) {
        TagRepository repository = repositoryFactory.createTagRepository();
        Tag tag = repository.findById(id).orElseThrow(TagNotFoundException::new);
        repository.remove(tag);
    }

    @Transactional
    public List<Tag> listTags() {
        List<Tag> tags = repositoryFactory.createTagRepository().findAll();
        if(tags.isEmpty()) throw new TagNotFoundException();
        return tags;
    }

    @Transactional
    public ArrayList<Tag> addTags(String[] t) {
        ArrayList<Tag> tags = new ArrayList<Tag>();
        for (int i = 0; i < t.length; i++)
            tags.add(new Tag(null, t[i].trim()));
        tags.forEach(tag -> tag.setId(addTag(tag).getId()));
        return tags;
    }

    @Transactional
    public Tag findById(int id) { return repositoryFactory.createTagRepository().findById(id).orElseThrow(TagNotFoundException::new);}
}
