package com.cezaram28.Assignment1.repository.jdbc;

import com.cezaram28.Assignment1.entity.Answer;
import com.cezaram28.Assignment1.entity.Question;
import com.cezaram28.Assignment1.entity.User;
import com.cezaram28.Assignment1.repository.AnswerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;

import javax.swing.text.html.Option;
import java.util.*;

@RequiredArgsConstructor
public class JdbcAnswerRepository implements AnswerRepository {

    private final JdbcTemplate template;

    @Override
    public List<Answer> findAll() {
        List<Answer> answers = template.query("SELECT * FROM answer", new AnswerMapper());
        for(Answer a : answers) {
            JdbcUserRepository jdbcUserRepository = new JdbcUserRepository(template);
            Optional<User> u = jdbcUserRepository.findById(a.getAuthor().getId());
            JdbcQuestionRepository jdbcQuestionRepository = new JdbcQuestionRepository(template);
            Optional<Question> q = jdbcQuestionRepository.findById(a.getQuestion().getId());
            if(u.isPresent())
                a.setAuthor(u.get());
            if(q.isPresent())
                a.setQuestion(q.get());;
        }
        return answers;
    }

    @Override
    public Optional<List<Answer>> findUsersAll(int userId) {
        List<Answer> answers = template.query("SELECT * FROM answer WHERE author_id=?", new AnswerMapper(), userId);
        for(Answer a : answers) {
            updateAnswer(a);
        }
        return Optional.ofNullable(answers);
    }

    @Override
    public Optional<List<Answer>> findQuestionsAll(int questionId) {
        List<Answer> answers = template.query("SELECT * FROM answer WHERE question_id=?", new AnswerMapper(), questionId);
        for(Answer a : answers) {
            updateAnswer(a);
        }
        return Optional.ofNullable(answers);
    }

    @Override
    public Answer save(Answer answer) {
        if (answer.getId() == null) {
            answer.setId(insert(answer));
        } else {
            update(answer);
        }
        return answer;
    }

    @Override
    public void remove(Answer answer) {
        template.update("DELETE FROM answer WHERE id=?", answer.getId());
    }

    public void updateAnswer(Answer a) {
        JdbcUserRepository jdbcUserRepository = new JdbcUserRepository(template);
        Optional<User> u = jdbcUserRepository.findById(a.getAuthor().getId());
        JdbcQuestionRepository jdbcQuestionRepository = new JdbcQuestionRepository(template);
        Optional<Question> q = jdbcQuestionRepository.findById(a.getQuestion().getId());
        if(u.isPresent())
            a.setAuthor(u.get());
        if(q.isPresent())
            a.setQuestion(q.get());
    }

    @Override
    public Optional<Answer> findById(int id) {
        List<Answer> answers = template.query("SELECT * FROM answer WHERE id=?", new AnswerMapper(), id);
        for(Answer a : answers) {
            updateAnswer(a);
        }
        return answers.isEmpty() ? Optional.empty() : Optional.ofNullable(answers.get(0));
    }

    private int insert(Answer answer) {
        SimpleJdbcInsert insert = new SimpleJdbcInsert(template);
        insert.setTableName("answer");
        insert.usingGeneratedKeyColumns("id");
        Map<String, Object> map = new HashMap<>();
        map.put("question_id", answer.getQuestion().getId());
        map.put("author_id", answer.getAuthor().getId());
        map.put("text", answer.getText());
        map.put("creation_date", answer.getCreationDate());
        map.put("vote_count", answer.getVoteCount());
        return insert.executeAndReturnKey(map).intValue();
    }

    private void update(Answer answer) {
        template.update("UPDATE answer SET question_id = ?, author_id = ?, text = ?, creation_date = ?, vote_count = ? WHERE id = ?",
                answer.getQuestion().getId(), answer.getAuthor().getId(), answer.getText(), answer.getCreationDate(), answer.getVoteCount(), answer.getId());
    }
}
