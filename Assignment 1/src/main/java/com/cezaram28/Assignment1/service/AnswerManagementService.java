package com.cezaram28.Assignment1.service;

import com.cezaram28.Assignment1.entity.Answer;
import com.cezaram28.Assignment1.entity.Question;
import com.cezaram28.Assignment1.exception.AnswerNotFoundException;
import com.cezaram28.Assignment1.repository.AnswerRepository;
import com.cezaram28.Assignment1.repository.RepositoryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AnswerManagementService {
    private final RepositoryFactory repositoryFactory;

    @Transactional
    public List<Answer> listAnswers() {
        return repositoryFactory.createAnswerRepository().findAll();
    }

    @Transactional
    public Optional<List<Answer>> listAllToQuestion(int questionId) {
        return repositoryFactory.createAnswerRepository().findQuestionsAll(questionId);
    }

    @Transactional
    public Optional<List<Answer>> listAllByUser(int userId) {
        return repositoryFactory.createAnswerRepository().findUsersAll(userId);
    }

    @Transactional
    public Answer addAnswer(Answer answer) {
        return repositoryFactory.createAnswerRepository().save(answer);
    }

    @Transactional
    public void removeAnswer(int id) {
        AnswerRepository repository = repositoryFactory.createAnswerRepository();
        Answer answer = repository.findById(id).orElseThrow(AnswerNotFoundException::new);
        repository.remove(answer);
    }

    @Transactional
    public Optional<Answer> findById(int id) {
        return repositoryFactory.createAnswerRepository().findById(id);
    }
}
