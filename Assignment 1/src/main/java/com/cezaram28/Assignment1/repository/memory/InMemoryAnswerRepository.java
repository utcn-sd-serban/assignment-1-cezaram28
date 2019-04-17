package com.cezaram28.Assignment1.repository.memory;

import com.cezaram28.Assignment1.entity.Answer;
import com.cezaram28.Assignment1.entity.Question;
import com.cezaram28.Assignment1.entity.User;
import com.cezaram28.Assignment1.repository.AnswerRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

public class InMemoryAnswerRepository implements AnswerRepository {
    private final Map<Integer, Answer> data = new ConcurrentHashMap<>();
    private final AtomicInteger currentId = new AtomicInteger(0);

    @Override
    public List<Answer> findAll() {
        return new ArrayList<>(data.values());
    }

    @Override
    public Optional<List<Answer>> findUsersAll(int userId) {
        return Optional.ofNullable(data.values().stream().filter(x->x.getAuthor().getId().equals(userId)).collect(Collectors.toList()));
    }

    @Override
    public Optional<List<Answer>> findQuestionsAll(int questionId) {
        return Optional.ofNullable(data.values().stream().filter(x->x.getQuestion().getId().equals(questionId)).collect(Collectors.toList()));
    }

    @Override
    public Answer save(Answer answer) {
        if (answer.getId() == null) {
            answer.setId(currentId.incrementAndGet());
        }
        data.put(answer.getId(), answer);
        return answer;
    }

    @Override
    public void remove(Answer answer) {
        data.remove(answer.getId());
    }

    @Override
    public Optional<Answer> findById(int id) {
        return Optional.ofNullable(data.get(id));
    }
}
