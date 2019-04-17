package com.cezaram28.Assignment1.repository.jdbc;

import com.cezaram28.Assignment1.entity.Answer;
import com.cezaram28.Assignment1.entity.Question;
import com.cezaram28.Assignment1.entity.User;
import com.cezaram28.Assignment1.entity.Vote;
import com.cezaram28.Assignment1.repository.VoteRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RequiredArgsConstructor
public class JdbcVoteRepository implements VoteRepository {

    private final JdbcTemplate template;

    @Override
    public Vote save(Vote vote) {
        if (vote.getId() == null) {
            vote.setId(insert(vote));
        } else {
            update(vote);
        }
        return vote;
    }

    @Override
    public void remove(Vote vote) {
        template.update("DELETE FROM vote WHERE id=?", vote.getId());
    }

    void updateVote(Vote v) {
        JdbcUserRepository jdbcUserRepository = new JdbcUserRepository(template);
        Optional<User> u = jdbcUserRepository.findById(v.getUser().getId());
        JdbcQuestionRepository jdbcQuestionRepository = new JdbcQuestionRepository(template);
        Optional<Question> q = jdbcQuestionRepository.findById(v.getQuestion().getId());
        JdbcAnswerRepository jdbcAnswerRepository = new JdbcAnswerRepository(template);
        Optional<Answer> a = jdbcAnswerRepository.findById(v.getAnswer().getId());
        if(u.isPresent())
            v.setUser(u.get());
        if(q.isPresent())
            v.setQuestion(q.get());
        if(a.isPresent())
            v.setAnswer(a.get());
    }

    @Override
    public Optional<Vote> findByQuestion(int questionId, int userId) {
        List<Vote> votes = template.query("SELECT * FROM vote WHERE question_id=? AND user_id=?", new VoteMapper(), questionId, userId);
        for(Vote v : votes) {
            updateVote(v);
        }
        return votes.isEmpty() ? Optional.empty() : Optional.ofNullable(votes.get(0));
    }

    @Override
    public Optional<Vote> findByAnswer(int answerId, int userId) {
        List<Vote> votes = template.query("SELECT * FROM vote WHERE answer_id=? AND user_id=?", new VoteMapper(), answerId, userId);
        for(Vote v : votes) {
            updateVote(v);
        }
        return votes.isEmpty() ? Optional.empty() : Optional.ofNullable(votes.get(0));
    }

    @Override
    public Optional<Vote> findById(int id) {
        List<Vote> votes = template.query("SELECT * FROM vote WHERE id=?", new VoteMapper(), id);
        for(Vote v : votes) {
            updateVote(v);
        }
        return votes.isEmpty() ? Optional.empty() : Optional.ofNullable(votes.get(0));
    }

    private int insert(Vote vote) {
        SimpleJdbcInsert insert = new SimpleJdbcInsert(template);
        insert.setTableName("vote");
        insert.usingGeneratedKeyColumns("id");
        Map<String, Object> map = new HashMap<>();
        map.put("type", vote.getType());
        if(vote.getQuestion() != null){
            map.put("question_id", vote.getQuestion().getId());
        } else {
            map.put("question_id", null);
        }

        if(vote.getAnswer() != null){
            map.put("answer_id", vote.getAnswer().getId());
        } else {
            map.put("answer_id", null);
        }
        map.put("user_id", vote.getUser().getId());
        return insert.executeAndReturnKey(map).intValue();
    }

    private void update(Vote vote) {
        template.update("UPDATE vote SET type = ?, question_id = ?, answer_id = ?, user_id = ? WHERE id = ?",
                vote.getType(), vote.getQuestion() != null ? vote.getQuestion().getId() : null,
                vote.getAnswer() != null ? vote.getAnswer().getId() : null, vote.getUser().getId(), vote.getId());
    }
}
