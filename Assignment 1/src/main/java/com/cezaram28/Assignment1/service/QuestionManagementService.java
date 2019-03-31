package com.cezaram28.Assignment1.service;

import com.cezaram28.Assignment1.entity.Question;
import com.cezaram28.Assignment1.entity.Tag;
import com.cezaram28.Assignment1.entity.User;
import com.cezaram28.Assignment1.exception.NoAdminException;
import com.cezaram28.Assignment1.exception.QuestionNotFoundException;
import com.cezaram28.Assignment1.repository.QuestionRepository;
import com.cezaram28.Assignment1.repository.RepositoryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class QuestionManagementService {
    private final RepositoryFactory repositoryFactory;

    @Transactional
    public List<Question> listQuestions() {
        List<Question> questions = repositoryFactory.createQuestionRepository().findAll();
        if(questions.isEmpty()) throw new QuestionNotFoundException();
        questions.sort((q1,q2)->q2.getCreationDate().compareTo(q1.getCreationDate()));
        return questions;
    }

    @Transactional
    public List<Question> listAllByUser(int userId) {
        Optional<List<Question>> questions = repositoryFactory.createQuestionRepository().findUsersAll(userId);
        if(!questions.isPresent()) throw new QuestionNotFoundException();
        questions.get().sort((q1,q2)->q2.getCreationDate().compareTo(q1.getCreationDate()));
        return questions.get();
    }

    @Transactional
    public Question addQuestion(Question question) {
        return repositoryFactory.createQuestionRepository().save(question);
    }

    @Transactional
    public Question editQuestion(Question question, User user) {
        if(!user.getIsAdmin()) throw new NoAdminException();
        return repositoryFactory.createQuestionRepository().save(question);
    }

    @Transactional
    public void removeQuestion(int id, User user) {
        if(!user.getIsAdmin()) throw new NoAdminException();
        QuestionRepository repository = repositoryFactory.createQuestionRepository();
        Question question = repository.findById(id).orElseThrow(QuestionNotFoundException::new);
        repository.remove(question);
    }

    @Transactional
    public Question findById(int id) {
        return repositoryFactory.createQuestionRepository().findById(id).orElseThrow(QuestionNotFoundException::new);
    }

    @Transactional
    public List<Question> getByTitle(String text) {
        List<Question> questions = listQuestions();
        questions = questions.stream().filter(question -> question.getTitle().contains(text)).collect(Collectors.toList());
        if(questions.isEmpty()) throw new QuestionNotFoundException();
        return questions;
    }

    @Transactional
    public List<Question> getByTag(String text) {
        List<Question> questions = new ArrayList<>();
        List<Question> qs = listQuestions();
        for(Question q : qs){
            for(Tag t : q.getTags()){
                if(t.getName().equals(text)){
                    questions.add(q);
                }
            }
        }
        if(questions.isEmpty()) throw new QuestionNotFoundException();
        return questions;
    }

}
