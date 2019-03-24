package com.cezaram28.Assignment1.service;

import com.cezaram28.Assignment1.entity.Vote;
import com.cezaram28.Assignment1.exception.VoteNotFoundException;
import com.cezaram28.Assignment1.repository.RepositoryFactory;
import com.cezaram28.Assignment1.repository.VoteRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class VoteManagementService {
    private final RepositoryFactory repositoryFactory;

    @Transactional
    public Vote save(Vote vote) {
        return repositoryFactory.createVoteRepository().save(vote);
    }

    @Transactional
    public void remove(int id) {
        VoteRepository repository = repositoryFactory.createVoteRepository();
        Vote vote = repository.findById(id).orElseThrow(VoteNotFoundException::new);
        repository.remove(vote);
    }

    @Transactional
    public Optional<Vote> findByQuestion(int questionId, int userId) {
        return repositoryFactory.createVoteRepository().findByQuestion(questionId, userId);
    }

    @Transactional
    public Optional<Vote> findByAnswer(int answerId, int userId) {
        return repositoryFactory.createVoteRepository().findByAnswer(answerId, userId);
    }
}
