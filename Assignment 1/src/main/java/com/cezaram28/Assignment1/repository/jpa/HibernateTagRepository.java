package com.cezaram28.Assignment1.repository.jpa;

import com.cezaram28.Assignment1.entity.Tag;
import com.cezaram28.Assignment1.repository.TagRepository;
import lombok.RequiredArgsConstructor;

import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
public class HibernateTagRepository implements TagRepository {
    private final EntityManager entityManager;

    @Override
    public List<Tag> findAll() {
        CriteriaBuilder builder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Tag> query = builder.createQuery(Tag.class);
        query.select(query.from(Tag.class));
        return entityManager.createQuery(query).getResultList();
    }

    @Override
    public Tag save(Tag tag) {
        CriteriaBuilder builder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Tag> query = builder.createQuery(Tag.class);
        Root<Tag> root = query.from(Tag.class);
        query.select(root).where(builder.equal(root.get("name"),tag.getName()));
        List<Tag> ts = entityManager.createQuery(query).getResultList();
        if(ts.isEmpty()) {
            entityManager.persist(tag);
            return tag;
        }
        return entityManager.merge(ts.get(0));
    }

    @Override
    public void remove(Tag tag) {
        entityManager.remove(tag);
    }

    @Override
    public Optional<Tag> findById(int id) {
        return Optional.ofNullable(entityManager.find(Tag.class, id));
    }
}
