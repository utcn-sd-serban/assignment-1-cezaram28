package com.cezaram28.Assignment1.repository.data;

import com.cezaram28.Assignment1.entity.Vote;
import com.cezaram28.Assignment1.repository.VoteRepository;
import org.springframework.data.repository.Repository;

import java.util.Optional;

public interface DataVoteRepository extends Repository<Vote, Integer>, VoteRepository {
    void delete(Vote vote);

    @Override
    default void remove(Vote vote) {
        delete(vote);
    }

    Optional<Vote> findByQuestionIdAndUserId(int questionId, int userId);

    @Override
    default Optional<Vote> findByQuestion(int questionId, int userId) {
        return findByQuestionIdAndUserId(questionId, userId);
    }

    Optional<Vote> findByAnswerIdAndUserId(int answerId, int userId);

    @Override
    default Optional<Vote> findByAnswer(int answerId, int userId) {
        return findByAnswerIdAndUserId(answerId, userId);
    }
}
