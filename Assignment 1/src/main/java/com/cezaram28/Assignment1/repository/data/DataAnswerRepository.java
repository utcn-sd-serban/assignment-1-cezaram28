package com.cezaram28.Assignment1.repository.data;

import com.cezaram28.Assignment1.entity.Answer;
import com.cezaram28.Assignment1.repository.AnswerRepository;
import org.springframework.data.repository.Repository;

import java.util.List;
import java.util.Optional;

public interface DataAnswerRepository extends Repository<Answer, Integer>, AnswerRepository {
    Optional<List<Answer>> findByAuthorId(int userId);

    @Override
    default Optional<List<Answer>> findUsersAll(int userId) {
        return findByAuthorId(userId);
    }

    Optional<List<Answer>> findByQuestionId(int questionId);

    @Override
    default Optional<List<Answer>> findQuestionsAll(int questionId) {
        return findByQuestionId(questionId);
    }

    void delete(Answer answer);

    @Override
    default void remove(Answer answer) {
        delete(answer);
    }
}
