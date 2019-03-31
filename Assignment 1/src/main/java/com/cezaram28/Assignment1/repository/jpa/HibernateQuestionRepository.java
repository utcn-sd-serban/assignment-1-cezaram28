package com.cezaram28.Assignment1.repository.jpa;

import com.cezaram28.Assignment1.entity.Question;
import com.cezaram28.Assignment1.repository.QuestionRepository;
import lombok.RequiredArgsConstructor;

import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
public class HibernateQuestionRepository implements QuestionRepository {
    private final EntityManager entityManager;

    @Override
    public List<Question> findAll() {
        CriteriaBuilder builder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Question> query = builder.createQuery(Question.class);
        query.select(query.from(Question.class));
        return entityManager.createQuery(query).getResultList();
    }

    @Override
    public Optional<List<Question>> findUsersAll(int userId) {
        CriteriaBuilder builder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Question> query = builder.createQuery(Question.class);
        Root<Question> root = query.from(Question.class);
        query.select(root).where(builder.equal(root.get("author").get("id"),userId));
        List<Question> qs = entityManager.createQuery(query).getResultList();
        if(qs.isEmpty()) return Optional.empty();
        return Optional.ofNullable(qs);
    }

    @Override
    public Question save(Question question) {
        if (question.getId() == null) {
            entityManager.persist(question);
            return question;
        } else {
            return entityManager.merge(question);
        }
    }

    @Override
    public void remove(Question question) {
        entityManager.remove(question);
    }

    @Override
    public Optional<Question> findById(int id) {
        return Optional.ofNullable(entityManager.find(Question.class, id));
    }
}
