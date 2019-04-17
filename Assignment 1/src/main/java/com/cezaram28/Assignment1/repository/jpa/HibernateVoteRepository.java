package com.cezaram28.Assignment1.repository.jpa;

import com.cezaram28.Assignment1.entity.Vote;
import com.cezaram28.Assignment1.repository.VoteRepository;
import lombok.RequiredArgsConstructor;

import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
public class HibernateVoteRepository implements VoteRepository {
    public final EntityManager entityManager;

    @Override
    public Vote save(Vote vote) {
        if (vote.getId() == null) {
            entityManager.persist(vote);
            return vote;
        } else {
            return entityManager.merge(vote);
        }
    }

    @Override
    public void remove(Vote vote) {
        entityManager.remove(vote);
    }

    @Override
    public Optional<Vote> findByQuestion(int questionId, int userId) {
        CriteriaBuilder builder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Vote> query = builder.createQuery(Vote.class);
        Root<Vote> root = query.from(Vote.class);
        query.select(root).where(builder.and(builder.equal(root.get("user").get("id"), userId),builder.equal(root.get("question").get("id"), questionId)));
        List<Vote> v = entityManager.createQuery(query).getResultList();
        if(!v.isEmpty()) return Optional.ofNullable(v.get(0));
        return Optional.empty();
    }

    @Override
    public Optional<Vote> findByAnswer(int answerId, int userId) {
        CriteriaBuilder builder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Vote> query = builder.createQuery(Vote.class);
        Root<Vote> root = query.from(Vote.class);
        query.select(root).where(builder.and(builder.equal(root.get("user").get("id"), userId),builder.equal(root.get("answer").get("id"), answerId)));
        List<Vote> v = entityManager.createQuery(query).getResultList();
        if(!v.isEmpty()) return Optional.ofNullable(v.get(0));
        return Optional.empty();
    }

    @Override
    public Optional<Vote> findById(int id) {
        return Optional.ofNullable(entityManager.find(Vote.class, id));
    }
}
