package com.cezaram28.Assignment1.repository.jpa;

import com.cezaram28.Assignment1.entity.Answer;
import com.cezaram28.Assignment1.repository.AnswerRepository;
import lombok.RequiredArgsConstructor;

import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
public class HibernateAnswerRepository implements AnswerRepository {
    private final EntityManager entityManager;

    @Override
    public List<Answer> findAll() {
        CriteriaBuilder builder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Answer> query = builder.createQuery(Answer.class);
        query.select(query.from(Answer.class));
        return entityManager.createQuery(query).getResultList();
    }

    @Override
    public Optional<List<Answer>> findUsersAll(int userId) {
        CriteriaBuilder builder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Answer> query = builder.createQuery(Answer.class);
        Root<Answer> root = query.from(Answer.class);
        query.select(root).where(builder.equal(root.get("author").get("id"),userId));
        List<Answer> as = entityManager.createQuery(query).getResultList();
        if(as.isEmpty()) return Optional.empty();
        return Optional.ofNullable(as);
    }

    @Override
    public Optional<List<Answer>> findQuestionsAll(int questionId) {
        CriteriaBuilder builder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Answer> query = builder.createQuery(Answer.class);
        Root<Answer> root = query.from(Answer.class);
        query.select(root).where(builder.equal(root.get("question").get("id"),questionId));
        List<Answer> as = entityManager.createQuery(query).getResultList();
        if(as.isEmpty()) return Optional.empty();
        return Optional.ofNullable(as);
    }

    @Override
    public Answer save(Answer answer) {
        if (answer.getId() == null) {
            entityManager.persist(answer);
            return answer;
        } else {
            return entityManager.merge(answer);
        }
    }

    @Override
    public void remove(Answer answer) {
        entityManager.remove(answer);
    }

    @Override
    public Optional<Answer> findById(int id) {
        return Optional.ofNullable(entityManager.find(Answer.class, id));
    }
}
