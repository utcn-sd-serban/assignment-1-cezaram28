package com.cezaram28.Assignment1.repository;

import com.cezaram28.Assignment1.entity.Tag;

import java.util.List;
import java.util.Optional;

public interface TagRepository {

    List<Tag> findAll();
    Tag save(Tag tag);
    void remove(Tag tag);
    Optional<Tag> findById(int id);
}
