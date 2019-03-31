package com.cezaram28.Assignment1.repository.data;

import com.cezaram28.Assignment1.entity.Tag;
import com.cezaram28.Assignment1.repository.TagRepository;
import org.springframework.data.repository.Repository;

import java.util.List;
import java.util.Optional;

public interface DataTagRepository extends Repository<Tag, Integer>, TagRepository {
    void delete(Tag tag);

    @Override
    default void remove(Tag tag) {
        delete(tag);
    }

}
