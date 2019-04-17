package com.cezaram28.Assignment1.repository.jdbc;

import com.cezaram28.Assignment1.entity.Question;
import com.cezaram28.Assignment1.entity.Tag;
import com.cezaram28.Assignment1.entity.User;
import com.cezaram28.Assignment1.repository.QuestionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;

import java.util.*;

@RequiredArgsConstructor
public class JdbcQuestionRepository implements QuestionRepository {

    private final JdbcTemplate template;

    @Override
    public List<Question> findAll() {
        List<Question> questions = template.query("SELECT * FROM question", new QuestionMapper());
        for(Question q : questions) {
            updateQuestion(q);
        }
        return questions;
    }

    @Override
    public Optional<List<Question>> findUsersAll(int userId) {
        List<Question> questions = template.query("SELECT * FROM question WHERE author_id = ?", new QuestionMapper(), userId);
        for(Question q : questions) {
           updateQuestion(q);
        }
        return Optional.ofNullable(questions);
    }

    @Override
    public Question save(Question question) {
        if (question.getId() == null) {
            question.setId(insert(question));
            insertTags(question);
        } else {
            update(question);
        }
        return question;
    }

    @Override
    public void remove(Question question) {
        template.update("DELETE FROM question WHERE id=?", question.getId());
    }

    public void updateQuestion(Question q) {
        JdbcUserRepository jdbcUserRepository = new JdbcUserRepository(template);
        Optional<User> u = jdbcUserRepository.findById(q.getAuthor().getId());
        if(u.isPresent())
            q.setAuthor(u.get());

        JdbcTagRepository jdbcTagRepository = new JdbcTagRepository(template);
        q.setTags(new ArrayList<>());
        getTagIds(q).forEach(tag -> q.getTags().add(jdbcTagRepository.findById(tag).get()));
    }

    @Override
    public Optional<Question> findById(int id) {
        List<Question> questions = template.query("SELECT * FROM question WHERE id = ?", new QuestionMapper(), id);
        for(Question q : questions) {
            updateQuestion(q);
        }
        return questions.isEmpty() ? Optional.empty() : Optional.ofNullable(questions.get(0));
    }

    private void insertTags(Question question) {
        SimpleJdbcInsert insert = new SimpleJdbcInsert(template);
        insert.setTableName("question_tags");
        Map<String, Object> map = new HashMap<>();
        for(Tag t : question.getTags()){
            map.put("question_id", question.getId());
            map.put("tag_id",t.getId());
            insert.execute(map);
        }
    }

    private int insert(Question question) {
        SimpleJdbcInsert insert = new SimpleJdbcInsert(template);
        insert.setTableName("question");
        insert.usingGeneratedKeyColumns("id");
        Map<String, Object> map = new HashMap<>();
        map.put("title", question.getTitle());
        map.put("author_id", question.getAuthor().getId());
        map.put("text", question.getText());
        map.put("creation_date", question.getCreationDate());
        map.put("vote_count", question.getVoteCount());
        return insert.executeAndReturnKey(map).intValue();
    }

    private void update(Question question) {
        template.update("UPDATE question SET title = ?, author_id = ?, text = ?, creation_date = ?, vote_count = ? WHERE id = ?",
                question.getTitle(), question.getAuthor().getId(), question.getText(),question.getCreationDate(), question.getVoteCount(), question.getId());
    }

    private List<Integer> getTagIds(Question question) {
        return template.query("SELECT tag_id FROM question_tags WHERE question_id = ?", (resultSet, i) -> resultSet.getInt("tag_id"), question.getId());
    }
}
