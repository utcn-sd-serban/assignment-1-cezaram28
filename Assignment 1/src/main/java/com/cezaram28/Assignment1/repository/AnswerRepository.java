package com.cezaram28.Assignment1.repository;

import com.cezaram28.Assignment1.entity.Answer;

import java.util.List;
import java.util.Optional;

public interface AnswerRepository {
    List<Answer> findAll();
    Optional<List<Answer>> findUsersAll(int userId);
    Optional<List<Answer>> findQuestionsAll(int questionId);
    Answer save(Answer answer);
    void remove(Answer answer);
    Optional<Answer> findById(int id);
}
