package com.cezaram28.Assignment1.repository.data;

import com.cezaram28.Assignment1.entity.Question;
import com.cezaram28.Assignment1.repository.QuestionRepository;
import org.springframework.data.repository.Repository;

import java.util.List;
import java.util.Optional;

public interface DataQuestionRepository extends Repository<Question, Integer>, QuestionRepository {
    Optional<List<Question>> findByAuthorId(int id);

    @Override
    default Optional<List<Question>> findUsersAll(int userId) {
        return findByAuthorId(userId);
    }

    void delete(Question question);

    @Override
    default void remove(Question question) {
        delete(question);
    }

}
