package com.cezaram28.Assignment1.repository.memory;

import com.cezaram28.Assignment1.entity.Tag;
import com.cezaram28.Assignment1.repository.TagRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;


public class InMemoryTagRepository implements TagRepository {

    private final Map<Integer, Tag> data = new ConcurrentHashMap<>();
    private final AtomicInteger currentId = new AtomicInteger(0);

    @Override
    public List<Tag> findAll() {
        return new ArrayList<>(data.values());
    }

    @Override
    public Tag save(Tag tag) {

        for(Tag t : data.values()){
            if(t.getName().equals(tag.getName()))
                return t;
        }

        if (tag.getId() == null){
            tag.setId(currentId.incrementAndGet());
        }
        data.put(tag.getId(), tag);
        return tag;
    }

    @Override
    public void remove(Tag tag) {
        data.remove(tag.getId());
    }

    @Override
    public Optional<Tag> findById(int id) {
        return Optional.ofNullable(data.get(id));
    }
}
