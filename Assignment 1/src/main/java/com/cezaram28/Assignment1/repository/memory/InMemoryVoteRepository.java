package com.cezaram28.Assignment1.repository.memory;

import com.cezaram28.Assignment1.entity.Vote;
import com.cezaram28.Assignment1.repository.VoteRepository;

import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

public class InMemoryVoteRepository implements VoteRepository {
    private final Map<Integer, Vote> data = new ConcurrentHashMap<>();
    private final AtomicInteger currentId = new AtomicInteger(0);

    @Override
    public Vote save(Vote vote) {
        if(vote.getId() == null) {
            vote.setId(currentId.incrementAndGet());
        }
        data.put(vote.getId(), vote);
        return vote;
    }

    @Override
    public void remove(Vote vote) {
        data.remove(vote.getId());
    }

    @Override
    public Optional<Vote> findByQuestion(int questionId, int userId) {
        for(Vote vote: data.values()) {
            if(vote.getQuestion() != null){
                if(questionId == vote.getQuestion().getId() && userId == vote.getUser().getId())
                    return Optional.ofNullable(vote);
            }
        }
        return Optional.empty();
    }

    @Override
    public Optional<Vote> findByAnswer(int answerId, int userId) {
        for(Vote vote: data.values()) {
            if(vote.getAnswer() != null){
                if(answerId == vote.getAnswer().getId() && userId == vote.getUser().getId())
                    return Optional.ofNullable(vote);
            }
        }
        return Optional.empty();
    }

    @Override
    public Optional<Vote> findById(int id) {
        return Optional.ofNullable(data.get(id));
    }
}
