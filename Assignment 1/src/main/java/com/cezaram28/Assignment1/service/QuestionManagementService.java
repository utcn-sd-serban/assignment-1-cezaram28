package com.cezaram28.Assignment1.service;

import com.cezaram28.Assignment1.entity.Question;
import com.cezaram28.Assignment1.entity.User;
import com.cezaram28.Assignment1.exception.QuestionNotFoundException;
import com.cezaram28.Assignment1.repository.QuestionRepository;
import com.cezaram28.Assignment1.repository.RepositoryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class QuestionManagementService {
    private final RepositoryFactory repositoryFactory;

    @Transactional
    public List<Question> listQuestions() {
        return repositoryFactory.createQuestionRepository().findAll();
    }

    @Transactional
    public Optional<List<Question>> listAllByUser(int userId) {
        return repositoryFactory.createQuestionRepository().findUsersAll(userId);
    }

    @Transactional
    public Question addQuestion(Question question) {
        return repositoryFactory.createQuestionRepository().save(question);
    }

    @Transactional
    public void removeQuestion(int id) {
        QuestionRepository repository = repositoryFactory.createQuestionRepository();
        Question question = repository.findById(id).orElseThrow(QuestionNotFoundException::new);
        repository.remove(question);
    }

    @Transactional
    public Optional<Question> findById(int id) {
        return repositoryFactory.createQuestionRepository().findById(id);
    }
}
