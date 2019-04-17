package com.cezaram28.Assignment1.repository;

import com.cezaram28.Assignment1.entity.Question;
import com.cezaram28.Assignment1.entity.User;

import java.util.List;
import java.util.Optional;

public interface QuestionRepository {
    List<Question> findAll();
    Optional<List<Question>> findUsersAll(int userId);
    Question save(Question question);
    void remove(Question question);
    Optional<Question> findById(int id);

}
