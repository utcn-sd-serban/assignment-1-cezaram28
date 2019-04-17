package com.cezaram28.Assignment1.repository.memory;

import com.cezaram28.Assignment1.entity.Question;
import com.cezaram28.Assignment1.entity.User;
import com.cezaram28.Assignment1.repository.QuestionRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

public class InMemoryQuestionRepository implements QuestionRepository {
    private final Map<Integer, Question> data = new ConcurrentHashMap<>();
    private final AtomicInteger currentId = new AtomicInteger(0);

    @Override
    public List<Question> findAll() {
        return new ArrayList<>(data.values());
    }

    @Override
    public Optional<List<Question>> findUsersAll(int userId) {
        return Optional.ofNullable(data.values().stream().filter(x->x.getAuthor().getId().equals(userId)).collect(Collectors.toList()));
    }

    @Override
    public Question save(Question question) {
        if (question.getId() == null) {
            question.setId(currentId.incrementAndGet());
        }
        data.put(question.getId(), question);
        return question;
    }

    @Override
    public void remove(Question question) {
        data.remove(question.getId());
    }

    @Override
    public Optional<Question> findById(int id) {
        return Optional.ofNullable(data.get(id));
    }
}
