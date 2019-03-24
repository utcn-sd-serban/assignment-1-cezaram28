package com.cezaram28.Assignment1.repository;

import com.cezaram28.Assignment1.entity.Vote;

import java.util.Optional;

public interface VoteRepository {
    Vote save(Vote vote);
    void remove(Vote vote);
    Optional<Vote> findByQuestion(int questionId, int userId);
    Optional<Vote> findByAnswer(int answerId, int userId);
    Optional<Vote> findById(int id);
}
